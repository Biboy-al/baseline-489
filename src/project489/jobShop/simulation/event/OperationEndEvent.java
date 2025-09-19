package project489.jobShop.simulation.event;

import project489.data.DoubleData;
import project489.jobShop.components.Job;
import project489.jobShop.components.Machine;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.evaluation.Simulation;
import project489.evaluation.Event;
import project489.problem.JobShopProblem;
import ec.EvolutionState;
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
                Job nextJob = machine.getWaitingJobs().removeFirst();
                jobShopSim.addEvent(new OperationStartEvent(super.getTime(), this.machine,nextJob));
            }else{
                int jobIndex = selectJobFromRule(this.machine.getWaitingJobs(), sim, (JobShopProblem ) sim.getProblem(), 0);
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
    public int selectJobFromRule(List<Job> waitingJobs, Simulation sim, JobShopProblem problem, int threadNum) {
        DoubleData input = new DoubleData();

        EvolutionState state = sim.getEvolutionState();
        GPIndividual ind = sim.getInd();


        int indexOfBestJob = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i<waitingJobs.size(); i++){
            problem.currentJob = waitingJobs.get(i);
            problem.simulation = sim;
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

