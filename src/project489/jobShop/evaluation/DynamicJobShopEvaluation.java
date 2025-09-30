package project489.jobShop.evaluation;

import ec.multiobjective.MultiObjectiveFitness;
import project489.evaluation.EvaluationModel;
import ec.EvolutionState;
import project489.evaluation.Simulation;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import ec.Problem;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaFitness;
import ec.util.Parameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class DynamicJobShopEvaluation extends EvaluationModel {



    private DynamicJobShopSimulation simulation;
    private ArrayList<DynamicJobShopSimulation> simulations;

    public static String P_SIM_BASE = "sim";

    public static String NUM_SIM = "sim.num";
    public static String REPLICATION_SIM = "sim.replication";
    public static String SEED_SIM = "sim.seed";
    public static String SEED_ROTATE = "simRotate";

    public static String OBJECTIVE = "objective";
    public static String METRIC = "metric";
    public static String MODE = "mode";

    public static String NUM_OF_MACHINES = "numOfMachines";
    public static String MAX_JOBS = "maxJobs";
    public static String REPLICATION = "replication";
    public static String WARMUP_JOBS = "warmupJobs";
    public static String SEED = "seed";
    public static String NUM_OPS = "numOps";
    public static String UTIL = "util";
    public static String DUE_DATE_ALLOWANCE = "DueDateAllowance";

    public int replication;
    private long seed;
    private long seedRotate;
    private String metric;
    private String mode;

    public DynamicJobShopEvaluation(EvolutionState state, Problem problem, Parameter base) {

        super(state, problem);

        this.simulations = new ArrayList<>();

        Parameter p = base.push(NUM_SIM);
        int simNum = state.parameters.getInt(p, null, 0);

        if(simNum <= 0) {
            System.err.println("Invalid num of simulations: " + simNum);
            System.exit(1);
        }

        // Gets the replication
        p = base.push(REPLICATION_SIM);
        this.replication = state.parameters.getInt(p, null, 0);

        // Gets the seed for the run
        p = base.push(SEED_SIM);
        this.seed = state.parameters.getLong(p, null, 0);

        // Gets the seed rotation
        p = base.push(SEED_ROTATE);
        this.seedRotate = state.parameters.getLong(p, null, 23);

        //Get objective
        Parameter o = base.push(OBJECTIVE);
        p = o.push(METRIC);
        this.metric = state.parameters.getString(p, null);

        p = o.push(MODE);
        this.mode = state.parameters.getString(p, null);

        for(int i = 0; i < simNum; i++) {
            Parameter b = base.push(P_SIM_BASE).push("" + i);

            p = b.push(NUM_OF_MACHINES);
            int numOfMachines = state.parameters.getInt(p, null, 5);

            p = b.push(MAX_JOBS);
            int maxJobs = state.parameters.getInt(p, null, 2500);


            p = b.push(WARMUP_JOBS);
            int warmupJobs = state.parameters.getInt(p, null, 500);

            p = b.push(NUM_OPS);
            int numOps = state.parameters.getInt(p, null, 10);

            p = b.push(UTIL);
            double util = state.parameters.getDouble(p, null, 0.8);

            p = b.push(DUE_DATE_ALLOWANCE);
            double dueDateAllowance = state.parameters.getDouble(p, null, 2.0);



            simulations.add(new DynamicJobShopSimulation(state, problem, 0,0,maxJobs, numOfMachines, seed, warmupJobs, numOps , util,dueDateAllowance));
        }

    }

    @Override
    public void evaluate(GPIndividual ind, EvolutionState evolutionState, int numOfRep) {

        switch(mode){

            //Find the mean of the objective
            case "mean": {
                double sumObjective = 0.0;

                // Run the simulation
                for (DynamicJobShopSimulation simulation : simulations) {

                    double objective = 0.0;
                    simulation.setRule(ind);

                    for (int i = 0; i < replication; i++) {

                        simulation.setSeed(this.seed + this.seedRotate * i);

                        simulation.run();

                        objective += getMetric(simulation);

                        simulation.clear();
                    }

                    sumObjective += objective / replication;

                }

                double meanObjective = sumObjective / simulations.size();

                KozaFitness fitness = (KozaFitness) ind.fitness;

                fitness.setStandardizedFitness(evolutionState, meanObjective);

                break;
            }

            case "max":{

                double sumObjective = 0.0;

                for (DynamicJobShopSimulation simulation : simulations) {
                    simulation.setRule(ind);

                    double maxObjective = Double.NEGATIVE_INFINITY;

                    for (int i = 0; i < replication; i++) {

                        simulation.setSeed(this.seed + this.seedRotate * i);

                        simulation.run();

                        maxObjective = Double.max(getMetric(simulation), Double.NEGATIVE_INFINITY);

                        simulation.clear();
                    }

                    sumObjective += maxObjective;

                }

                double meanObjective = sumObjective / simulations.size();

                KozaFitness fitness = (KozaFitness) ind.fitness;

                fitness.setStandardizedFitness(evolutionState, meanObjective);

                break;
            }
        }




    }


    public double getMetric(DynamicJobShopSimulation simulation) {

        switch (this.metric){

            case "tardiness": {
                return simulation.getMeanTardiness();

            }
            case "flowtime": {
                return simulation.getMeanFlowTime();

            }
            default: {
                System.err.println("Invalid objective: " + this.metric);
                System.exit(1);
            }
        }

        return 0;
    }

    @Override
    public double evaluateForStats(GPIndividual ind, EvolutionState evolutionState, int numOfRep) {

        this.simulation = new DynamicJobShopSimulation(state, problem, 0,0,10, 10, this.seed + numOfRep, 10, 5 ,1, 5);

        double sumFlowTime = 0;

        for (int i = 0; i < numOfRep; i++) {
            this.simulation.run();
            sumFlowTime += this.simulation.getMeanFlowTime();
        }

        return sumFlowTime / numOfRep;

    }

}
