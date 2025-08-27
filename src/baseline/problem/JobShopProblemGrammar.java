package baseline.problem;

import baseline.evaluation.EvaluationModel;
import baseline.evaluation.Simulation;
import baseline.jobShop.components.Job;
import baseline.jobShop.evaluation.DynamicJobShopEvaluation;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.*;
import ec.gp.ge.GEIndividual;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import ec.gp.ge.GESpecies;

public class JobShopProblemGrammar extends GPProblem implements SimpleProblemForm{

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
        if (individual.evaluated) return;

        GEIndividual geInd = (GEIndividual) individual;
        GESpecies species = (GESpecies) geInd.species;
        GPTree[] trees = new GPTree[1];
        trees[0] = new GPTree();
        int pos = species.makeTrees(evolutionState, geInd, trees, i1, null);

        if (pos == GESpecies.BIG_TREE_ERROR) {
            System.out.println("Failed to build GPTree!");
            return;
        }

        System.out.println(trees[0].child.makeLispTree());

//        GPIndividual ind = new GPIndividual();
//        ind.species = (GPSpecies) evolutionState.population.subpops[0].species;
//
//        //calculates the fitness
//        evaluationModel.evaluate(ind, evolutionState, this.replication);

        individual.evaluated = true;
    }

    public EvaluationModel getEvaluationModel() {
        return evaluationModel;
    }
}
