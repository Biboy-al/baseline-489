package baseline.evaluation;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.GPIndividual;

public abstract class Simulation {
    protected int seed;
    protected EvolutionState state;
    protected GPIndividual ind;
    protected Problem problem;
    int ii;
    int i1;


    public Simulation(EvolutionState state, GPIndividual ind, Problem problem, int i, int i1, int seed) {
        this.state = state;
        this.ind = ind;
        this.problem = problem;
        this.ii = i;
        this.i1 = i1;
        this.seed = seed;
    }

    public GPIndividual getInd() {
        return ind;
    }

    public Problem getProblem() {
        return problem;
    }

    public EvolutionState getEvolutionState() {
        return state;
    }

    public int getIi() {
        return ii;
    }

    public int geti1() {
        return i1;
    }

    public abstract double startSimulation();


}
