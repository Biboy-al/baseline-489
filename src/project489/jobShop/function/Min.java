package project489.jobShop.function;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import project489.data.DoubleData;

public class Min extends GPNode {
    @Override
    public String toString() {
        return "min";
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData re = (DoubleData) gpData;

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double childrenOutput1 = re.value;

        children[1].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double childrenOutput2 = re.value;

        re.value = Math.min(childrenOutput1, childrenOutput2);
    }
}
