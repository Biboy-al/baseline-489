package baseline.problem;

import baseline.evaluation.EvaluationModel;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.GPProblem;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import baseline.jobShop.components.Job;

public class JobShopProblem extends GPProblem implements SimpleProblemForm{

    String P_EVAL_MODEL = "eval-model";

    //inits the evaluation model
    private EvaluationModel evaluationModel;
    public Job currentJob;

    //Called by ECJ before evolution begins
    @Override
    public void setup(EvolutionState state, final Parameter base) {
        super.setup(state, base);

        //creates the path, where the evalauton.model is defined
//        Parameter p = base.push(P_EVAL_MODEL);
//
//        //initates the evaluation model found in the param file
//        evaluationModel = (EvaluationModel) state.parameters.getInstanceForParameter(p, null, EvaluationModel.class);

        //evaluationModel.setup(state, p);
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
        //cast the indivudal to a GP indivudal
        GPIndividual indi = (GPIndividual) individual;

        //calls the evalutation method, and gives fitness
        //evaluationModel.evalute(indi.fitness, rule, state);

        DynamicJobShopSimulation sim = new DynamicJobShopSimulation(evolutionState, indi, this, i, i1, 2500, 10);
        double meanFlowTime = sim.startSimulation();
        KozaFitness fitness = (KozaFitness) indi.fitness;

        fitness.setStandardizedFitness(evolutionState, meanFlowTime);
//        System.out.println("Tree:\n" + indi.trees[0].child.makeLispTree());
//        System.out.println("Fitness: " + meanFlowTime);
        individual.evaluated = true;
    }
}
