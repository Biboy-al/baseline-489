package baseline.stats;

import baseline.evaluation.EvaluationModel;
import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaFitness;
import ec.gp.koza.KozaShortStatistics;
import ec.simple.SimpleStatistics;
import ec.util.Parameter;

public class CustomStats extends KozaShortStatistics {

    private int replication;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);

        Parameter param = new Parameter("baseline.stats.replication");

        this.replication = state.parameters.getInt(param, null, 1);
    }

    @Override
    public void postEvaluationStatistics(EvolutionState state) {

        super.postEvaluationStatistics(state);

        GPIndividual bestInd = (GPIndividual) bestOfGeneration[0];
        JobShopProblem problem = (JobShopProblem) state.evaluator.p_problem;

        EvaluationModel evaluationModel = problem.getEvaluationModel();
        System.out.println(bestInd.fitness.fitness());
//        double rawFitness = ((KozaFitness) bestInd.fitness).standardizedFitness();
//        System.out.println("Standardized Fitness of best individual (as set by evaluate()): " + rawFitness);

        double meanFlowTime = 0.0;
        for(int i = 1; i < this.replication; i++) {
            meanFlowTime += evaluationModel.startEvaluation(bestInd);
        }

        double averageFlowTime = meanFlowTime / this.replication;

        System.out.println("Average Flow Time (Of best Individual with  "+ this.replication +") replications: " + averageFlowTime);
    }
}
