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

        this.machine.releaseCurrentJob(((DynamicJobShopSimulation) sim).getCurrentTime());
        this.job.finishCurrentOperation();

        // If current job is not finished, reintegrate into event lifecycle
        if (!this.job.isFinished()) {
            jobShopSim.addEvent(new JobArrivalEvent(super.getTime(), this.job));
        } else {
            this.job.setDepartureTime(super.getTime());
            jobShopSim.addCompletedJob(this.job);
        }
        // Set current time for waiting jobs

        for (Job job : machine.getWaitingJobs()) {
            job.setCurrentTime(super.getTime());
        }

        JobShopProblem problem = (JobShopProblem) sim.getProblem();

        if (!machine.getWaitingJobs().isEmpty()) {
            Job nextJob;

            if (problem == null) {
                nextJob = machine.getWaitingJobs().get(0);
            } else {

                int jobIndex = selectJobFromRule(this.machine.getWaitingJobs(), sim, problem, 0);
                nextJob = machine.getWaitingJobs().get(jobIndex);
            }

            machine.removeJob(nextJob);

            jobShopSim.addEvent(new OperationStartEvent(super.getTime(), this.machine, nextJob));
        }
    }

    @Override
    public int getPriority() {
        return 1;
    }

    // Returns index of best job
    public int selectJobFromRule(List<Job> waitingJobs, Simulation sim, JobShopProblem problem, int threadNum) {
        DoubleData input = new DoubleData();

        EvolutionState state = sim.getEvolutionState();
        GPIndividual ind = sim.getInd();

        int indexOfBestJob = 0;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < waitingJobs.size(); i++) {
            problem.currentJob = waitingJobs.get(i);

            if (problem.currentJob.isFinished()) continue;

            problem.simulation = sim;
            input.value = 0;
            ind.trees[0].child.eval(state, threadNum, input, null, ind, problem);

            if (input.value > bestScore) {
                bestScore = input.value;
                indexOfBestJob = i;
            }
        }

        return indexOfBestJob;
    }
}