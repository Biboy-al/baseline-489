package baseline.jobShop.components;

public class Job {

    private int arrivalTime;
    private int departureTime;
    private Operation[] operations = new Operation[10];
    int currentOperation = 0;

    private Job(Integer arrivalTime, Operation[] operations) {
        this.arrivalTime = arrivalTime;
        this.operations = operations;
    }

    public static Job generateJob(int arrivalTime, Machine[] machines) {
        return new Job(arrivalTime, Operation.generateOperations(machines));
    }

    public Operation[] getOperations() {
        return operations;
    }

    public Operation getCurrentOperation() {
        return operations[currentOperation];
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isFinished(){
        return currentOperation == operations.length - 1;
    }

    public void finishCurrentOperation() {
        currentOperation++;
    }

    public int getCurrentOpeationIndex() {
        return currentOperation;
    }

}
