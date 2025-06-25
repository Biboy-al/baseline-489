package baseline.evaluation;


import ec.EvolutionState;
import ec.Problem;
import ec.gp.GPIndividual;

/**Class that evaluates the an indivudal **/
public abstract class EvaluationModel {

    protected EvolutionState state;
    protected Problem problem;
    private double seed;

    public EvaluationModel(EvolutionState state, Problem problem) {
        this.state = state;
        this.problem = problem;
        this.seed = 0;
    }



    public abstract double startEvaluation(GPIndividual ind);

}
