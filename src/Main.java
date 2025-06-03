import ec.EvolutionState;
import ec.Evolve;
import ec.util.Output;
import ec.util.Parameter;
import ec.util.ParameterDatabase;

import java.security.KeyStore;

public class Main extends Evolve {
    public static void main(String[] args) {

        //used to store GP run info
        EvolutionState state;

        //used to load params of GP from a params file
        ParameterDatabase params;

        //Loads the parameters from java arguments
        params = loadParameterDatabase(args);

        //defines how many jobs to do from the param file.
        //If it can't find any, init 1
        int numJob = params.getIntWithDefault(new Parameter("jobs"), null, 1);

        if (numJob < 0) Output.initialError("Jobs should be greater than 0");

        for (int job = 0; job < numJob; job++) {

            //returns an evolution state
            // internally sets up the population, breeding piepline, and random generators

            if (params == null) params = loadParameterDatabase(args);

            state = initialize(params, job);
            state.output.systemMessage("Job " + job);

            // stores an object array with job number
            state.job = new Object[1];
            state.job[0] = job;

            //stores command line arg for the run
            state.runtimeArguments = args;

            //info about multi job gp runs
            //add prefixs to all output and checkpoint files
            if (numJob >1){
                String jobFilePrefix = "Job." + job + ".";
                state.output.setFilePrefix(jobFilePrefix);
                state.checkpointPrefix = jobFilePrefix + state.checkpointPrefix;
            }

            //clean up the state
            state.run(EvolutionState.C_STARTED_FRESH);
            cleanup(state);
            params = null;


        }


    }
}