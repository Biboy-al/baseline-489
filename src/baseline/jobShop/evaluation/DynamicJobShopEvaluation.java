package baseline.jobShop.evaluation;

import baseline.evaluation.EvaluationModel;
import baseline.evaluation.Simulation;
import ec.EvolutionState;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import ec.Problem;
import ec.gp.GPIndividual;
import ec.util.Parameter;

public class DynamicJobShopEvaluation extends EvaluationModel {

    private DynamicJobShopSimulation simulation;
    private int maxJobs;
    private int numOfMachines;

    public DynamicJobShopEvaluation(EvolutionState state, Problem problem) {

        super(state, problem);

        this.simulation = null;

        Parameter numOfMachinesParams = new Parameter("eval.problem.numOfMachines");
        Parameter maxJobsParam = new Parameter("eval.problem.maxJobs");


        this.numOfMachines = state.parameters.getInt(numOfMachinesParams, null, 5);
        this.maxJobs = state.parameters.getInt(maxJobsParam, null,1000);

    }

    @Override
    public double startEvaluation(GPIndividual ind) {

        this.simulation = new DynamicJobShopSimulation(state, ind,problem, 0,0,this.maxJobs, this.numOfMachines );
        return this.simulation.startSimulation();
    }

}
