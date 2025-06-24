package baseline.jobShop.function;

import baseline.data.DoubleData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class If extends GPNode {
    public static int expectedChildren = 3;

    public static int getExpectedChildren() { return expectedChildren; }

    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {

        DoubleData re = (DoubleData) gpData;

        children[0].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);

        double childrenOutput = re.value;

        if(childrenOutput > 0){
            children[1].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);
        }else{
            children[2].eval(evolutionState, i, gpData, adfStack, gpIndividual, problem);
        }
    }

    public String toString() {
        return "if";
    }
}
