package baseline.jobShop.simulation;

import baseline.evaluation.Simulation;
import baseline.jobShop.components.Machine;
import baseline.jobShop.components.Job;
import baseline.evaluation.Event;
import baseline.jobShop.components.Operation;
import baseline.jobShop.simulation.event.JobArrivalEvent;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.gp.GPIndividual;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class DynamicJobShopSimulation extends Simulation {

    final ArrayList<Job> allJob;
    final ArrayList<Job> waitingJob;
    final Machine[] machines;
    final Queue<Event> eventQueue;
    final int numOfJobs;
    double time = 0;
    public DynamicJobShopSimulation(EvolutionState state, GPIndividual ind, Problem problem, int ii, int i1, int numOfJobs, int numOfMachines, int seed) {
        super(state, ind, problem, ii, i1, seed);
        //start simulation with 10 operations
        Operation.setSeed(seed);
        allJob = new ArrayList<Job>();
        waitingJob = new ArrayList<Job>();
        eventQueue = new PriorityQueue<Event>();
        machines = populateMachines(numOfMachines);
        this.numOfJobs = numOfJobs;
    }

    public double startSimulation() {

        eventQueue.add(new JobArrivalEvent(time, Job.generateJob(time, machines)));
        //As long as num of jobs has not been satsified keep generating new ones

        //Process events as they come
        while(!eventQueue.isEmpty()) {
            Event nextEvent = eventQueue.poll();
            //Set the time from the event
            time = nextEvent.getTime();
            nextEvent.evalute(this);

            //Keep generating new jobs
            if (numOfJobs > allJob.size()) {
                Job newJob = Job.generateJob(time, machines);
                eventQueue.add(new JobArrivalEvent(time, newJob));
            }
        }

        List<Double> flowTimes = new ArrayList<Double>();

        // Calculate mean flow
        for (Job job : allJob) {
            double flowTime = job.getDepartureTime() - job.getArrivalTime();
            flowTimes.add(flowTime);
        }

        //calculate mean flow time
        return  flowTimes.stream().mapToDouble(Double::doubleValue).sum() / flowTimes.size();

    }

    private Machine[] populateMachines(int machineCount) {
        Machine[] machines = new Machine[machineCount];
        for (int i = 0; i < machineCount; i++) {
            machines[i] = new Machine(i);
        }
        return machines;
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
}
