package project489.jobShop.components;

import project489.evaluation.Simulation;
import project489.jobShop.simulation.DynamicJobShopSimulation;

public class Job {

    //Used to calculate mean flow
    private double arrivalTime;
    private double departureTime;
    private double arrivalInQueueTime;
    private double currentTime;
    private double dueDate;
    private Operation[] operations = new Operation[10];
    int currentOperation = 0;

    private Job(double arrivalTime, Operation[] operations, DynamicJobShopSimulation sim) {

        double totalOperationTime = 0;

        for (Operation operation : operations) {
            totalOperationTime += operation.getProcessingTime();
        }

        this.arrivalTime = arrivalTime;
        this.operations = operations;
        this.dueDate = arrivalTime + (sim.getDueDateAllowance() * totalOperationTime);
        this.arrivalInQueueTime = 0;
    }

    public static Job generateJob(double arrivalTime, Machine[] machines, DynamicJobShopSimulation sim) {
        return new Job(arrivalTime, Operation.generateOperations(machines), sim);
    }

    public Operation[] getOperations() {
        return operations;
    }

    public Operation getCurrentOperation() {
        return operations[currentOperation];
    }



    /**
     * GETTERS AND SETTERS FOR TIME
     * **/

    public double getDueDate() { return dueDate;}

    public double getArrivalTime() {
        return arrivalTime;
    }

    public double getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(double departureTime) {
        this.departureTime = departureTime;
    }

    public double getArrivalInQueueTime() {
        return arrivalInQueueTime;
    }

    public void setArrivalInQueueTime(double arrivalInQueueTime) {
        this.arrivalInQueueTime = arrivalInQueueTime;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double currentTime) {
        this.currentTime = currentTime;
    }

    public boolean isFinished(){
        return currentOperation > operations.length - 1;
    }

    public void finishCurrentOperation() {
        currentOperation++;
    }

    public int getCurrentOpeationIndex() {
        return currentOperation;
    }

    public int getNumberOfOperationsLeft() {
        return (operations.length - currentOperation) - 1 ;
    }



    public double getRemainingProcessingTime() {
        double remainingTime = 0;
        for(int i = currentOperation; i < operations.length; i++) {
            remainingTime += operations[i].getProcessingTime();
        }
        return remainingTime;
    }

}
