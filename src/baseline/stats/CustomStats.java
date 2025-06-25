package baseline.stats;

import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaShortStatistics;
import ec.simple.SimpleStatistics;

public class CustomStats extends KozaShortStatistics {

    @Override
    public void postEvaluationStatistics(EvolutionState state) {
        super.postEvaluationStatistics(state);

        int replications = 30;

        GPIndividual bestInd = (GPIndividual) bestOfGeneration[0];
        JobShopProblem problem = (JobShopProblem) state.evaluator.p_problem;

        double meanFlowTime = 0.0;
        for(int i = 1; i < replications; i++) {
            meanFlowTime += problem.startSimulation(state, bestInd, 2500, 10);
        }

        double averageFlowTime = meanFlowTime / replications;

        System.out.println("Average Flow Time Of best Individual with  "+ replications +" replications: " + averageFlowTime);
    }
}
