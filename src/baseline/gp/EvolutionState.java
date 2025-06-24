package baseline.gp;

import ec.gp.GPNode;
import ec.simple.SimpleEvolutionState;
import ec.util.Checkpoint;
import ec.util.Parameter;

import java.util.ArrayList;
import java.util.List;

//Tailors the evolutionary state to the domain it's testing

/***
 *
 * Class that controls the main evolutionary loop.
 * */
public class EvolutionState extends SimpleEvolutionState {

    //are keys to find from the param file
    protected final static String P_TERMINALS_FROM = "terminals-from";
    protected final static String P_INCLUDE_ERC = "include-erc";

    //defines source of terminals
    protected String terminalsFrom;
    protected boolean includeErc;
    //used for seeding randomness for job generations
    protected long jobSeed;
    //set of avaliable GP terminals
    protected List<GPNode> terminals;

    List<Double> genTimes = new ArrayList<Double>();


    /**
     * Initalise the GP state and fetch meta data
     * @param state state of the gp run
     * @param base parameterDatabase used to fetch data from .param file
     * **/
    @Override
    public void setup(ec.EvolutionState state, Parameter base) {
       Parameter p;
       //Get the seed from the param file
       p = new Parameter("seed").push("0");
       jobSeed =  parameters.getLongWithDefault(p, null, 0L);

       super.setup(this, base);
    }

    /**
     * Main loop of the GP. it dicates how to start the gp,
     * and what to do in one run
     * @param condition what type of condition is it
     * **/
    @Override
    public void run(int condition) {
        if (condition == C_STARTED_FRESH) {
            //function that dicates generation of pop, and evaluators
            //ensures that gp starts with a clean slate
            startFresh();
        }else{
            //start from a clean checkpoint
            startFromCheckpoint();
        }

        int result = R_NOTDONE;
            while (result ==  R_NOTDONE){
                result = evolve();
            }
            finish(result);
    }

    /**
     * Evolves the entire population. Uses the evalutor
     * to apply the problem to each indivudal. It then
     * applies breeding i.e picks parent, and use
     * genetic operators.
     * **/
    @Override
    public int evolve() {

        statistics.preEvaluationStatistics(this);
        //Evaluate population, applying the problem on each indivudal
        //assigning a fitness
        evaluator.evaluatePopulation(this);
        statistics.postEvaluationStatistics(this);

        String exchangerWantsToShutdown = exchanger.runComplete(this);
        if (exchangerWantsToShutdown!=null)
        {
            output.message(exchangerWantsToShutdown);
            /*
             * Don't really know what to return here.  The only place I could
             * find where runComplete ever returns non-null is
             * IslandExchange.  However, that can return non-null whether or
             * not the ideal individual was found (for example, if there was
             * a communication error with the server).
             *
             * Since the original version of this code didn't care, and the
             * result was initialized to R_SUCCESS before the while loop, I'm
             * just going to return R_SUCCESS here.
             */

            return R_SUCCESS;
        }

        if(generation >= numGenerations -1 ){
            return R_SUCCESS;
        }

        statistics.preBreedingStatistics(this);
        //Breeding selected parent from selection mechanism, and apply genetic operators
        //return a new set of indivudals
        population = breeder.breedPopulation(this);
        statistics.postBreedingStatistics(this);


        //If there are parallel ec with multiple sub pops here are where it gets opt in
        statistics.prePostBreedingExchangeStatistics(this);
        population = exchanger.postBreedingExchangePopulation(this);
        statistics.postPostBreedingExchangeStatistics(this);


         //If there are multiple scenarious rotate or change it for next population
//        // Generate new instances if needed
//        RuleOptimizationProblem problem = (RuleOptimizationProblem)evaluator.p_problem;
//        if (problem.getEvaluationModel().isRotatable()) {
//            problem.rotateEvaluationModel();
//        }

        // INCREMENT GENERATION AND CHECKPOINT
        generation++;

        //Adds a checkpoint to maybe load later if failed
        if (checkpoint && generation%checkpointModulo == 0)
        {
            output.message("Checkpointing");
            statistics.preCheckpointStatistics(this);
            Checkpoint.setCheckpoint(this);
            statistics.postCheckpointStatistics(this);
        }

        return R_NOTDONE;


    }

}
