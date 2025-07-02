package baseline.problem;

import baseline.evaluation.EvaluationModel;
import baseline.jobShop.evaluation.DynamicJobShopEvaluation;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
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

    //Called by ECJ before evolution begins
    @Override
    public void setup(EvolutionState state, final Parameter base) {
        super.setup(state, base);

        Parameter replicationParam = new Parameter("eval.problem.replication");

        this.replication = state.parameters.getInt(replicationParam, null, 1);

        this.evaluationModel = new DynamicJobShopEvaluation(state,this);

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

        GPIndividual ind = (GPIndividual) individual;

        double totalMeanFlow = 0.0;
        for(int j = 0; j < this.replication; j++){
            totalMeanFlow +=  evaluationModel.startEvaluation(ind);
        }
        //Set Fitness
        double meanFlowTime = totalMeanFlow / this.replication;
        double[] meanFlowTimes= {meanFlowTime};

//        KozaFitness fitness = (KozaFitness) ind.fitness;
        MultiObjectiveFitness fitness = (MultiObjectiveFitness) ind.fitness;
        fitness.setObjectives(evolutionState, meanFlowTimes);

//        fitness.setStandardizedFitness(evolutionState, meanFlowTime);
//        MultiObjectiveFitness f = (MultiObjectiveFitness)fitness;
        individual.evaluated = true;
    }

    public EvaluationModel getEvaluationModel() {
        return evaluationModel;
    }
}
