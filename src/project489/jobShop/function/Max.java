package project489.jobShop.function;

import project489.data.DoubleData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Max extends GPNode {
    public static int expectedChildren = 2;

    public static int getExpectedChildren() { return expectedChildren; }

    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData re = (DoubleData) gpData;

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double childrenOutput1 = re.value;

        children[1].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double childrenOutput2 = re.value;

        re.value = Math.max(childrenOutput1, childrenOutput2);

    }

    public String toString() {
        return "max";
    }
}
