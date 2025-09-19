package project489.jobShop.terminal;

import project489.data.DoubleData;
import project489.jobShop.components.Job;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class LLD extends GPNode {


    @Override
    public String toString() {
        return "LLD";
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        DoubleData rd = (DoubleData) gpData;

        JobShopProblem jobShopProblem = (JobShopProblem) problem;

        Job currentJob = jobShopProblem.currentJob;
        DynamicJobShopSimulation sim = (DynamicJobShopSimulation) jobShopProblem.simulation;

        rd.value = currentJob.getDueDate() - sim.getCurrentTime() - currentJob.getRemainingProcessingTime();
    }
}
