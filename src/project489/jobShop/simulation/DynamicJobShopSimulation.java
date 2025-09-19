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

    private final int SEED_ROTATE = 1000;

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

    RandomDataGenerator rng;

    private int numOfRuns = 0;
    private double time = 0;


    // Track job generation
    private int jobsGenerated = 0;
    private double nextJobArrivalTime = 0;

    public DynamicJobShopSimulation(EvolutionState state, GPIndividual ind, Problem problem,
                                    int ii, int i1, int numOfJobs, int numOfMachines,
                                    int seed, int warmupJobs, int numOps, double util) {
        super(state, ind, problem, ii, i1, seed);
        Operation.setSeed(seed);
        this.allJob = new ArrayList<Job>();
        this.waitingJob = new ArrayList<Job>();
        this.eventQueue = new PriorityQueue<Event>();
        this.numOfMachines = numOfMachines;
        this.numOfJobs = numOfJobs;
        this.warmupJobs = warmupJobs;
        this.util = util;
        this.numOps = numOps;
        machines = populateMachines(numOfMachines);

        this.rng  = new RandomDataGenerator();

        int numSamples = 500;
        this.meanProcTime = estimateMeanProcessingTime(numSamples);

        this.meanInterArrival = (this.numOps * meanProcTime) / (this.util * this.numOfMachines);
    }

    public void run() {
        if(numOfRuns > 0){
            clear();
            Operation.setSeed(this.seed + SEED_ROTATE);
        }

        // Generate first job at time 0
        generateNextJob();

        // Process events until we have enough completed jobs
        while(!eventQueue.isEmpty() && completedJobCount < numOfJobs) {

            Event nextEvent = eventQueue.poll();
            this.time = nextEvent.getTime();
            nextEvent.evalute(this);
        }

        numOfRuns++;
    }

    // FIXED: Generate jobs with proper time intervals
    public void generateNextJob() {
        if (jobsGenerated < numOfJobs + warmupJobs) {
            // Use current nextJobArrivalTime, then calculate next one
            double arrivalTime = nextJobArrivalTime;

            Job newJob = Job.generateJob(arrivalTime, machines);
            eventQueue.add(new JobArrivalEvent(arrivalTime, newJob));

            double interArrivalTime = generateInterArrivalTime();

            System.out.println(interArrivalTime);
            nextJobArrivalTime = arrivalTime + interArrivalTime;

            jobsGenerated++;
        }
    }

    private double generateInterArrivalTime() {
        return this.rng.nextExponential(this.meanInterArrival);
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
        this.nextJobArrivalTime = 0; // Reset arrival time

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

    public boolean hasReachedTotalJob(){
        return completedJobCount > numOfJobs;
    }
}