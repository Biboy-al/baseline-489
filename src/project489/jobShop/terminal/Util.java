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
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.problem.JobShopProblem;

public class Util extends GPNode {

    public String toString() {
        return "Util";
    }

    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData data = (DoubleData) gpData;
        JobShopProblem jobShopProblem = (JobShopProblem) problem;
        Job job = jobShopProblem.currentJob;

        DynamicJobShopSimulation dynamicJobShopSimulation = (DynamicJobShopSimulation) jobShopProblem.simulation;

//        get current machine
        Machine machine = dynamicJobShopSimulation.getMachine(job.getCurrentOperation().getMachineId());

        data.value = machine.getUtilization(dynamicJobShopSimulation.getCurrentTime());

    }
}
