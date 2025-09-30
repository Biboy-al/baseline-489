package project489.jobShop.function;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import project489.data.DoubleData;

public class Equal extends GPNode {

    @Override
    public String toString() {
        return "Equal";
    }

    public String name() {
        return "Equal";
    }

    @Override
    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData re = (DoubleData) gpData;

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double reValue = re.value;

        children[1].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        if(reValue == re.value) {
            re.value = 0;
        }else{
            re.value = 1;
        }

    }
}
