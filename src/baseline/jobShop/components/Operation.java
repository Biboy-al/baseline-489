package baseline.jobShop.components;

import java.util.*;
import java.util.stream.IntStream;


public class Operation {

    private final int machineId;
    private final double processingTime;
    private static Random random = new Random();

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    private Operation(int machineId, int processingTime) {
        this.machineId = machineId;
        this.processingTime = processingTime;
    }

    public static Operation[] generateOperations(Machine[] machines) {

        //shuffle the machine ids
        Machine[] shuffledMachine = machines.clone();
        Collections.shuffle(Arrays.asList(shuffledMachine), random);
        Operation[] operations = new Operation[machines.length];

        //randomly assign an operation for a given machine
        for (int i = 0; i < shuffledMachine.length; i++) {

            operations[i] = new Operation(shuffledMachine[i].getMachineId(), random.nextInt(48) + 1);
        }

        //return operations
        return operations;
    }


    public int getMachineId() {
        return machineId;
    }

    public double getProcessingTime() {
        return processingTime;
    }
}
