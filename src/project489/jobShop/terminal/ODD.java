package project489.jobShop.terminal;

import project489.data.DoubleData;
import project489.jobShop.components.Operation;
import project489.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import project489.jobShop.components.Job;

public class ODD extends GPNode {
    public String toString() {
        return "ODD";
    }

    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData data = (DoubleData) gpData;

        JobShopProblem jobShopProblem = (JobShopProblem) problem;

        Job currentJob = jobShopProblem.currentJob;

        double dueDate = currentJob.getDueDate();

        // Set value
        data.value = dueDate;
    }
}
