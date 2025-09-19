package project489.jobShop.evaluation;

import project489.evaluation.EvaluationModel;
import ec.EvolutionState;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import ec.Problem;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaFitness;
import ec.util.Parameter;

import java.util.Random;

public class DynamicJobShopEvaluation extends EvaluationModel {

    private DynamicJobShopSimulation simulation;
    private int maxJobs;
    private int numOfMachines;
    private int seed;
    private int warmupJobs;

    public DynamicJobShopEvaluation(EvolutionState state, Problem problem) {

        //For now only takes in account one
        super(state, problem);

        this.simulation = null;

        Parameter numOfMachinesParams = new Parameter("eval.problem.numOfMachines");
        Parameter maxJobsParam = new Parameter("eval.problem.maxJobs");
        Parameter seedParams = new Parameter("eval.problem.seed");
        Parameter numOfWarmupJobsParam = new Parameter("eval.problem.warmupJobs");


        this.numOfMachines = state.parameters.getInt(numOfMachinesParams, null, 5);
        this.maxJobs = state.parameters.getInt(maxJobsParam, null,1000);
        this.seed = state.parameters.getInt(seedParams, null, new Random().nextInt());
        this.warmupJobs = state.parameters.getInt(numOfWarmupJobsParam, null, 0);

    }

    @Override
    public void evaluate(GPIndividual ind, EvolutionState evolutionState, int numOfRep) {

        this.simulation = new DynamicJobShopSimulation(state, ind,problem, 0,0,this.maxJobs, this.numOfMachines, this.seed, 500);

        double sumTardiness = 0;

        for (int i = 0; i < numOfRep; i++) {
            this.simulation.run();
            sumTardiness += this.simulation.getMeanTardiness();

        }

        double meanTardiness = sumTardiness / numOfRep;


//        double[] meanTardiness_a = {meanTardiness};

//        MultiObjectiveFitness fitness = (MultiObjectiveFitness) ind.fitness;

        KozaFitness fitness = (KozaFitness) ind.fitness;

        System.out.println("Fitness: " + meanTardiness);

        fitness.setStandardizedFitness(evolutionState, meanTardiness);

//        fitness.setObjectives(evolutionState, meanTardiness_a);



    }

    @Override
    public double evaluateForStats(GPIndividual ind, EvolutionState evolutionState, int numOfRep) {

        this.simulation = new DynamicJobShopSimulation(state, ind,problem, 0,0,this.maxJobs, this.numOfMachines, this.seed + numOfRep, this.warmupJobs);

        double sumFlowTime = 0;

        for (int i = 0; i < numOfRep; i++) {
            this.simulation.run();
            sumFlowTime += this.simulation.getMeanFlowTime();
        }

        return sumFlowTime / numOfRep;

    }

}
