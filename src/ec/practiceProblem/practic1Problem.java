package ec.practiceProblem;

import ec.EvolutionState;
import ec.Individual;
import ec.gp.GPProblem;

public class practic1Problem extends GPProblem {
    public void evaluate(EvolutionState evolutionState,
                         Individual ind,
                         int i,
                         int i1) {

        if (ind.evaluated) return;


        double totalError = 0.0;
        int numSamples = 20;

    }
}

