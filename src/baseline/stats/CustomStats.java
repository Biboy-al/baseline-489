package baseline.stats;

import baseline.evaluation.EvaluationModel;
import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPTree;
import ec.gp.ge.GEIndividual;
import ec.gp.ge.GESpecies;
import ec.simple.SimpleStatistics;
import ec.util.Parameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CustomStats extends SimpleStatistics {

    private int replication;
    private PrintWriter csvWriter;
    private boolean wroteHeader = false;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);
        Parameter param = new Parameter("baseline.stats.replication");
        this.replication = state.parameters.getInt(param, null, 1);

        try {
            csvWriter = new PrintWriter(new FileWriter("stats.csv", false));
        } catch (IOException e) {
            state.output.fatal("Failed to open CSV file for writing: " + e);
        }
    }

    @Override
    public void postEvaluationStatistics(EvolutionState state) {
        super.postEvaluationStatistics(state);

        JobShopProblem problem = (JobShopProblem) state.evaluator.p_problem;
        EvaluationModel evaluationModel = problem.getEvaluationModel();

        // Find best individual
        Individual bestInd = getBestIndividual(state);

        // Translate to GP individual if needed
        GPIndividual gpInd = translateToGPIndividual(state, bestInd);

        double bestFitness = gpInd.fitness.fitness();
        double averageFlowTime = evaluationModel.evaluateForStats(gpInd, state, this.replication);

        // Write header once
        if (!wroteHeader) {
            csvWriter.println("generation,best_fitness,average_flow_time,best_individual_fitness");
            wroteHeader = true;
        }

        // Write one line per generation with new column for best individual fitness
        csvWriter.printf("%d,%.6f,%.6f,%.6f%n",
                state.generation,
                bestFitness,
                averageFlowTime,
                bestFitness  // new column: best individual's fitness
        );
        csvWriter.flush();

        // Optional human-readable output
        state.output.println("Best individual's fitness: " + bestFitness, 0);
        state.output.println("Average Flow Time (best individual, "
                + this.replication + " reps): " + averageFlowTime, 0);
        state.output.println("Best Individual (mapped to GP form):", 0);
        gpInd.printIndividualForHumans(state, 0);
    }

    @Override
    public void finalStatistics(EvolutionState state, int result) {
        super.finalStatistics(state, result);
        if (csvWriter != null) {
            csvWriter.close();
        }
    }

    private Individual getBestIndividual(EvolutionState state) {
        Individual best = state.population.subpops.get(0).individuals.get(0);
        for (int subpop = 0; subpop < state.population.subpops.size(); subpop++) {
            for (int ind = 0; ind < state.population.subpops.get(subpop).individuals.size(); ind++) {
                Individual candidate = state.population.subpops.get(subpop).individuals.get(ind);
                if (candidate.fitness.betterThan(best.fitness)) {
                    best = candidate;
                }
            }
        }
        return best;
    }

    private GPIndividual translateToGPIndividual(EvolutionState state, Individual ind) {
        if (ind instanceof GEIndividual) {
            GEIndividual geInd = (GEIndividual) ind;
            GESpecies species = (GESpecies) geInd.species;

            GPTree[] trees = new GPTree[1];
            trees[0] = new GPTree();
            species.makeTrees(state, geInd, trees, 0, null);

            GPIndividual gpInd = new GPIndividual();
            gpInd.trees = trees;
            gpInd.fitness = geInd.fitness;  // share fitness object
            return gpInd;
        } else {
            return (GPIndividual) ind;
        }
    }
}
