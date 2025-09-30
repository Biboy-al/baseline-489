package project489.jobShop.simulation;

import org.apache.commons.math3.random.RandomDataGenerator;
import project489.evaluation.Simulation;
import project489.jobShop.components.Machine;
import project489.jobShop.components.Job;
import project489.evaluation.Event;
import project489.jobShop.components.Operation;
import project489.jobShop.simulation.event.JobArrivalEvent;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.GPIndividual;

import java.util.*;

public class DynamicJobShopSimulation extends Simulation {

//    private final int SEED_ROTATE = 1000;

    final ArrayList<Job> allJob;
    final ArrayList<Job> waitingJob;
    Machine[] machines;
    final Queue<Event> eventQueue;
    final int numOfMachines;
    final int numOfJobs;
    final int warmupJobs;
    private double totalFlowTime;
    private double totalTardiness;
    private double meanProcTime;
    private double util;
    private double meanInterArrival;
    private int completedJobCount;
    private int numOps;

    private double dueDateAllowance;

    // FIXED: Adjusted minimum arrival time and added validation
    private double minArrivalTime = 10.0; // Increased from 5

    RandomDataGenerator rng;

    private int numOfRuns = 0;
    private double time = 0;

    // Track job generation
    private int jobsGenerated = 0;
    private double nextJobArrivalTime = 0;

    public DynamicJobShopSimulation(EvolutionState state, Problem problem,
                                    int ii, int i1, int numOfJobs, int numOfMachines,
                                    long defaultSeed, int warmupJobs, int numOps, double util, double dueDateAllowance) {
        super(state, problem, ii, i1, defaultSeed);

        this.allJob = new ArrayList<Job>();
        this.waitingJob = new ArrayList<Job>();
        this.eventQueue = new PriorityQueue<Event>();
        this.numOfMachines = numOfMachines;
        this.numOfJobs = numOfJobs;
        this.warmupJobs = warmupJobs;
        this.util = Math.min(0.95, Math.max(0.1, util));
        this.numOps = numOps;
        machines = populateMachines(numOfMachines);

        this.dueDateAllowance = dueDateAllowance;
        this.rng = new RandomDataGenerator();

        setSeed(defaultSeed);

        this.meanProcTime =  estimateMeanProcessingTime(1000);

        this.meanInterArrival = interArrivalTimeMean(numOfMachines, numOps, util);

    }

    public void run() {

        generateNextJob();

        while(!eventQueue.isEmpty()) {
            Event nextEvent = eventQueue.poll();
            this.time = nextEvent.getTime();
            nextEvent.evalute(this);
        }

    }

    public double interArrivalTimeMean(int numMachines, int numOps, double utilLevel) {

        return ((double) numOps * this.meanProcTime) / (utilLevel * numMachines);
    }

    public void generateNextJob() {
        if (jobsGenerated < numOfJobs + warmupJobs) {

            // Use current nextJobArrivalTime, then calculate next one
            double arrivalTime = nextJobArrivalTime;

            Job newJob = Job.generateJob(arrivalTime, machines, this);
            eventQueue.add(new JobArrivalEvent(arrivalTime, newJob));

            double interArrivalTime = generateInterArrivalTime();
            this.nextJobArrivalTime = arrivalTime + interArrivalTime;


            jobsGenerated++;
        }
    }

    // FIXED: Generate inter-arrival time like reference code
    private double generateInterArrivalTime() {
        // Use exponential distribution with the calculated mean
        double interArrival = this.rng.nextExponential(this.meanInterArrival);

        // Apply reasonable bounds to prevent extreme values
        double minBound = this.meanInterArrival * 0.01; // Allow very small intervals
        double maxBound = this.meanInterArrival * 10.0; // Cap extremely long intervals

        return Math.max(minBound, Math.min(interArrival, maxBound));
    }

    public void clear(){
        this.allJob.clear();
        this.waitingJob.clear();
        this.eventQueue.clear();

        this.totalFlowTime = 0;
        this.totalTardiness = 0;
        this.completedJobCount = 0;
        this.jobsGenerated = 0;
        this.time = 0;
        this.nextJobArrivalTime = 0;

        for (Machine machine : machines) {
            machine.reset();
        }
    }

    private Machine[] populateMachines(int machineCount) {
        Machine[] machines = new Machine[machineCount];
        for (int i = 0; i < machineCount; i++) {
            machines[i] = new Machine(i);
        }
        return machines;
    }

    public void addCompletedJob(Job job) {
        // Only count jobs after warmup period
        if (allJob.size() >= warmupJobs) {

            completedJobCount++;

            double flowTime = job.getDepartureTime() - job.getArrivalTime();
            double tardiness = Math.max(0, job.getDepartureTime() - job.getDueDate());

            totalFlowTime += flowTime;
            totalTardiness += tardiness;
        }
    }

    private double estimateMeanProcessingTime(int samples) {
        double total = 0.0;
        for (int i = 0; i < samples; i++) {
            Operation op = Operation.generationOperation();
            total += op.getProcessingTime();
        }
        return total / samples;
    }

    public void addEvent(Event e){
        eventQueue.add(e);
    }

    public void addJob(Job job) {
        allJob.add(job);
    }

    public Machine getMachine(int index){
        return machines[index];
    }

    public double getCurrentTime(){
        return time;
    }

    public double getMeanFlowTime() {
        return completedJobCount > 0 ? totalFlowTime / completedJobCount : 0.0;
    }

    public double getMeanTardiness() {

        return completedJobCount > 0 ? totalTardiness / completedJobCount : 0.0;
    }

    public double getDueDateAllowance() {
        return dueDateAllowance;
    }

    public void setSeed(long seed) {
        this.seed = seed ;
        Operation.setSeed(this.seed);
        this.rng.reSeed(this.seed);

    }

    public double getNumOfMachines(){
        return numOfMachines;
    }

    public long getSeed(){
        return this.seed;
    }

}