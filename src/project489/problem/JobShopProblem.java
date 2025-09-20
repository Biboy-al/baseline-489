package project489.problem;

import ec.gp.koza.KozaFitness;
import project489.evaluation.EvaluationModel;
import project489.evaluation.Simulation;
import project489.jobShop.evaluation.DynamicJobShopEvaluation;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import project489.jobShop.components.Job;

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

        this.evaluationModel = new DynamicJobShopEvaluation(state, this, base);

    }

    /**
     * This is where an indivudal is tested and assigned a fitness
     * @param EvolutionState the global state of the run, containing GP
     * @param indi the individual from the population that is being evaluated
     * @param subPopulation index of the subpopulation belongs to
     * @param ThreadNum index of the thread calling this method
     * **/
    public void evaluate(EvolutionState evolutionState, Individual individual, int i, int i1) {

        if (individual.evaluated) return;

        GPIndividual gpInd = (GPIndividual) individual;

        evaluationModel.evaluate(gpInd, evolutionState, this.replication);
        individual.fitness = gpInd.fitness;
        individual.evaluated = true;
    }

    public EvaluationModel getEvaluationModel() {
        return evaluationModel;
    }
}
