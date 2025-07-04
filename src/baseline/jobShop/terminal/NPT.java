package baseline.jobShop.terminal;

import baseline.data.DoubleData;
import baseline.jobShop.components.Operation;
import baseline.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import baseline.jobShop.components.Job;

public class NPT extends GPNode {
    public String toString() {
        return "NPT";
    }

    public void eval(EvolutionState evolutionState, int i, GPData gpData, ADFStack adfStack, GPIndividual gpIndividual, Problem problem) {
        JobShopProblem jobShopProblem = (JobShopProblem) problem;
        DoubleData rd = (DoubleData) gpData;
        Job currentJob = jobShopProblem.currentJob;

        Operation[] operation = currentJob.getOperations();
        int operationIndex = currentJob.getCurrentOpeationIndex();

        //if no next operation set 0
        if(operationIndex+1 > operation.length){
            rd.value = 0;

            //If there is a next operation give it the next time
        }else{
           rd.value = operation[operationIndex+1].getProcessingTime();
        }
    }
}
