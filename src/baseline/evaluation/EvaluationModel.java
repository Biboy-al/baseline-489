package baseline.evaluation;


import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.gp.GPIndividual;
import ec.gp.ge.GEIndividual;

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

    public abstract void evaluate(GPIndividual ind, EvolutionState evolutionState, int numOfRep);



    public abstract double evaluateForStats(GPIndividual ind, EvolutionState evolutionState, int numOfRep);

}
