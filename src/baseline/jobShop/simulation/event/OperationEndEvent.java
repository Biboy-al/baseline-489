package baseline.jobShop.simulation.event;

import baseline.data.DoubleData;
import baseline.jobShop.components.Job;
import baseline.jobShop.components.Machine;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import baseline.evaluation.Simulation;
import baseline.evaluation.Event;
import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;

import java.util.List;

public class OperationEndEvent extends Event {

    Machine machine;
    Job job;
    public OperationEndEvent(double time, Machine machine, Job job) {
        super(time, 1);
        this.machine = machine;
        this.job = job;
    }

    @Override
    public void evalute(Simulation sim) {
        DynamicJobShopSimulation jobShopSim = (DynamicJobShopSimulation) sim;
        this.machine.releaseCurrentJob();
        this.job.finishCurrentOperation();

        //if current job is not finished
        //reintegrate the new job into the event lifecycle
        if(!this.job.isFinished()){
            jobShopSim.addEvent(new JobArrivalEvent(super.getTime(), this.job));
        }else{
            //if it is finished, set departure time
            //And break from event loop
            this.job.setDepartureTime(super.getTime());
        }

        //Set current time
        for(Job job: machine.getWaitingJobs()){
            job.setCurrentTime(super.getTime());
        }

        JobShopProblem problem = (JobShopProblem) sim.getProblem();

        if(!machine.getWaitingJobs().isEmpty()){


            //If not being used by ecj deafult to first in first out rule
            if(problem == null){
                Job nextJob = machine.getWaitingJobs().remove(0);
                jobShopSim.addEvent(new OperationStartEvent(super.getTime(), this.machine,nextJob));
            }else{
                int jobIndex = selectJobFromRule(this.machine.getWaitingJobs(), sim.getInd(),sim.getEvolutionState(), (JobShopProblem ) sim.getProblem(), 0);
                Job nextJob = machine.getWaitingJobs().remove(jobIndex);
                jobShopSim.addEvent(new OperationStartEvent(super.getTime(), this.machine,nextJob));
            }

        }

    }
    @Override
    public int getPriority() {
        return 1;
    }

    //Returns index of best job
    public int selectJobFromRule(List<Job> waitingJobs, GPIndividual ind, EvolutionState state, JobShopProblem problem, int threadNum) {
        DoubleData input = new DoubleData();

        int indexOfBestJob = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i<waitingJobs.size(); i++){
            problem.currentJob = waitingJobs.get(i);
            input.value = 0;
            ind.trees[0].child.eval(state, threadNum, input, null, ind, problem);

            if(input.value > bestScore){
                bestScore = input.value;
                indexOfBestJob = i;
            }

        }
        return indexOfBestJob;
    }
}

