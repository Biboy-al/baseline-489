package baseline.problem;

import baseline.evaluation.EvaluationModel;
import baseline.evaluation.Simulation;
import baseline.jobShop.evaluation.DynamicJobShopEvaluation;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
import ec.gp.GPTree;
import ec.gp.ge.GEIndividual;
import ec.gp.ge.GESpecies;
import ec.gp.koza.KozaFitness;
import ec.multiobjective.MultiObjectiveFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import baseline.jobShop.components.Job;

public class JobShopProblem extends GPProblem implements SimpleProblemForm{

    String P_EVAL_MODEL = "eval-model";

    //inits the evaluation model
    private EvaluationModel evaluationModel;
    private int replication;
    public Job currentJob;
    public Simulation simulation;

    //Called by ECJ before evolution begins
    @Override
    public void setup(EvolutionState state, final Parameter base) {
        super.setup(state, base);

        Parameter replicationParam = new Parameter("eval.problem.replication");

        this.replication = state.parameters.getInt(replicationParam, null, 1);

        this.evaluationModel = new DynamicJobShopEvaluation(state, this);

    }

    /**
     * This is where an indivudal is tested and assigned a fitness
     * @param EvolutionState the global state of the run, containing GP
     * @param indi the individual from the population that is being evaluated
     * @param subPopulation index of the subpopulation belongs to
     * @param ThreadNum index of the thread calling this method
     * **/
    public void evaluate(EvolutionState evolutionState, Individual individual, int i, int i1) {
        //If already evaluated break


        if (individual.evaluated) {
            System.out.println("SKIPPING - Already evaluated!");
            return;
        }


//        if(individual instanceof GEIndividual){
//            GEIndividual geInd = (GEIndividual) individual;
//
//            GESpecies species = (GESpecies) geInd.species;
//            GPTree[] trees = new GPTree[1];
//            trees[0] = new GPTree();
//
//            GPIndividual gpInd = (GPIndividual) species.gpspecies.newIndividual(evolutionState, i1);
//
//            int pos = species.makeTrees(evolutionState, geInd, gpInd.trees, i1, null);
//
//            gpInd.fitness = geInd.fitness;
//
//            evolutionState.output.println(
//                    "GEN " + evolutionState.generation +
//                            " IND (GE) TREE " + gpInd.trees[0].child.makeLispTree(),
//                    0); // "0" means write to standard statistics file
//
//
//            //calculates the fitness
//            evaluationModel.evaluate(gpInd, evolutionState, this.replication);
//            individual.fitness = gpInd.fitness;
//        } else{

            GPIndividual gpInd = (GPIndividual) individual;

            evolutionState.output.println(
                    "GEN " + evolutionState.generation +
                            " IND (GE) TREE " + gpInd.trees[0].child.makeLispTree(),
                    0); // "0" means write to standard statistics file


            evaluationModel.evaluate(gpInd, evolutionState, this.replication);
            individual.fitness = gpInd.fitness;
//        }

//        System.out.println(individual.fitness.fitness());

        individual.evaluated = true;
    }

    public EvaluationModel getEvaluationModel() {
        return evaluationModel;
    }
}
