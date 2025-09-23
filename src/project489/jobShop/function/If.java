package project489.jobShop.function;

import project489.data.DoubleData;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class If extends GPNode {
    // Only 3 children now: condition, then, else
    public static int expectedChildren = 3;

    public static int getExpectedChildren() {
        return expectedChildren;
    }

    @Override
    public void eval(EvolutionState state, int thread, GPData input,
                     ADFStack stack, GPIndividual individual, Problem problem) {

        DoubleData data = (DoubleData) input;

        // Evaluate condition (child 0)
        children[0].eval(state, thread, data, stack, individual, problem);
        double condition = data.value;

        if (condition > 0) {
            // True branch (child 1)
            children[1].eval(state, thread, data, stack, individual, problem);
        } else {
            // False branch (child 2)
            children[2].eval(state, thread, data, stack, individual, problem);
        }
    }

    @Override
    public String toString() {
        return "if";
    }
}
