package project489.stats;

import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPTree;
import ec.gp.ge.GEIndividual;
import ec.gp.ge.GESpecies;
import ec.gp.koza.KozaFitness;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.SimpleStatistics;
import ec.util.Parameter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class CustomStats extends SimpleStatistics {

    private int replication;
    private PrintWriter generationWriter;  // Only for per-generation stats
    private boolean wroteGenHeader = false;

    @Override
    public void setup(EvolutionState state, Parameter base) {
        super.setup(state, base);
        Parameter param = new Parameter("baseline.stats.replication");
        this.replication = state.parameters.getInt(param, null, 1);

        param = new Parameter("stat.child.0.file_name");
        String filename = state.parameters.getString(param, null);

        try {
            generationWriter = new PrintWriter(new FileWriter(filename, false));
        } catch (IOException e) {
            state.output.fatal("Failed to open CSV file for writing: " + e);
        }
    }

    @Override
    public void postEvaluationStatistics(EvolutionState state) {
        super.postEvaluationStatistics(state);

        // --- Find best individual of the generation ---
        Individual bestInd = getBestIndividual(state);
        GPIndividual gpInd = translateToGPIndividual(state, bestInd);

        // --- Compute mean fitness ---
        double totalFitness = 0.0;
        int totalCount = 0;
        for (int subpop = 0; subpop < state.population.subpops.size(); subpop++) {
            for (Individual ind : state.population.subpops.get(subpop).individuals) {
                KozaFitness kf = (KozaFitness) ind.fitness;
                totalFitness += kf.standardizedFitness();
                totalCount++;
            }
        }
        double meanFitness = totalFitness / totalCount;

        // --- Best fitness as string ---
        KozaFitness bestKF = (KozaFitness) gpInd.fitness;
        String bestFitnessStr = String.valueOf(bestKF.standardizedFitness());

        // --- Write one line per generation ---
        if (!wroteGenHeader) {
            generationWriter.println("generation,best_individual_fitness,mean_fitness,tree");
            wroteGenHeader = true;
        }

        StringBuilder bestTreeBuilder = new StringBuilder();
        for (int t = 0; t < gpInd.trees.length; t++) {
            bestTreeBuilder.append(gpInd.trees[t].child.makeLispTree());
            if (t < gpInd.trees.length - 1) {
                bestTreeBuilder.append(" | ");
            }
        }
        String bestTreeString = "\"" + bestTreeBuilder.toString().replace("\"", "\"\"") + "\"";

        generationWriter.printf("%d,%s,%.5f,%s%n",
                state.generation, bestFitnessStr, meanFitness, bestTreeString);
        generationWriter.flush();

        // --- Console output ---
        state.output.println("Best individual's fitness: " + bestFitnessStr, 0);
        state.output.println("Mean fitness this generation: " + meanFitness, 0);
        state.output.println("Best Individual:", 0);
        gpInd.printIndividualForHumans(state, 0);
    }

    @Override
    public void finalStatistics(EvolutionState state, int result) {
        super.finalStatistics(state, result);
        if (generationWriter != null) generationWriter.close();
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

            // Convert fitness to KozaFitness if needed
            KozaFitness kf = new KozaFitness();
            if (geInd.fitness instanceof KozaFitness) {
                kf.setStandardizedFitness(state, ((KozaFitness) geInd.fitness).standardizedFitness());
            } else {
                // fallback: assign 0
                kf.setStandardizedFitness(state, 0.0);
            }
            gpInd.fitness = kf;

            return gpInd;
        } else {
            return (GPIndividual) ind;
        }
    }
}
