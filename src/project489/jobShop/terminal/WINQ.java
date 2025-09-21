package project489.jobShop.terminal;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import project489.data.DoubleData;
import project489.jobShop.components.Job;
import project489.jobShop.components.Machine;
import project489.jobShop.components.Operation;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.problem.JobShopProblem;

public class WINQ extends GPNode {
    @Override
    public String toString() {
        return "WINQ";
    }

    @Override
    public void eval(
            EvolutionState evolutionState,
            int thread,
            GPData gpData,
            ADFStack adfStack,
            GPIndividual gpIndividual,
            Problem problem) {

        DoubleData data = (DoubleData) gpData;
        JobShopProblem jobShopProblem = (JobShopProblem) problem;
        Job currentJob = jobShopProblem.currentJob;

        DynamicJobShopSimulation sim = (DynamicJobShopSimulation) jobShopProblem.simulation;
        int nextMachineIndex = currentJob.getCurrentOpeationIndex()+1;

        if (currentJob == null || currentJob.isFinished() || nextMachineIndex >= sim.getNumOfMachines()) {
            data.value = 0.0;
            return;
        }

        Machine nextMachine = sim.getMachine(nextMachineIndex);

        // Sum up processing times of all jobs waiting in the next machine
        double totalWork = 0.0;
        for (Job waitingJob : nextMachine.getWaitingJobs()) {
            Operation op = waitingJob.getCurrentOperation();
            if (op != null) {
                totalWork += op.getProcessingTime();
            }
        }

        data.value = totalWork;
    }
}
