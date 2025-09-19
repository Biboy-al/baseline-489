package project489.jobShop.terminal;

import project489.data.DoubleData;
import project489.jobShop.components.Operation;
import project489.problem.JobShopProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import project489.jobShop.components.Job;

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

        if(operationIndex+1 > operation.length-1){
            rd.value = 0;

        }else{
           rd.value = operation[operationIndex+1].getProcessingTime();
        }
    }
}
