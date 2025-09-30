package project489.jobShop.components;

import java.util.*;


public class Operation {

    private final int machineId;
    private final double processingTime;
    private static Random random = new Random();

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    private Operation(int machineId, double processingTime) {
        this.machineId = machineId;
        this.processingTime = processingTime;
    }

    public static Operation[] generateOperations(Machine[] machines) {

        //shuffle the machine ids
        Machine[] shuffledMachine = machines.clone();
        Collections.shuffle(Arrays.asList(shuffledMachine), Operation.random);
        Operation[] operations = new Operation[machines.length];

        //randomly assign an operation for a given machine
        for (int i = 0; i < shuffledMachine.length; i++) {
            operations[i] = new Operation(shuffledMachine[i].getMachineId(), sampleProcessingTime());
        }

        //return operations
        return operations;
    }

    public static Operation generationOperation() {
        return new Operation(0, sampleProcessingTime());
    }


    public int getMachineId() {
        return machineId;
    }

    public double getProcessingTime() {
        return processingTime;
    }

    private static double sampleProcessingTime() {

        double lambda = 1.0 / 20.0;
        return -Math.log(1 - random.nextDouble()) / lambda;

    }
}
