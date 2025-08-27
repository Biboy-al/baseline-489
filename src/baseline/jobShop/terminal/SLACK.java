package baseline.jobShop.terminal;

import baseline.data.DoubleData;
import baseline.jobShop.components.Job;
import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class SLACK extends GPNode {
    @Override
    public String toString() {
        return "SLACK";
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        DoubleData rd = (DoubleData) gpData;

        JobShopProblem jobShopProblem = (JobShopProblem) problem;

        Job currentJob = jobShopProblem.currentJob;

        rd.value = currentJob.getDueDate() - (currentJob.getCurrentTime() + currentJob.getArrivalInQueueTime());
    }
}
