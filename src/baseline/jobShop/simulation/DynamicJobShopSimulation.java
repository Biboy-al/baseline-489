package baseline.jobShop.simulation;

import baseline.jobShop.components.Machine;
import baseline.jobShop.components.Job;
import baseline.jobShop.simulation.event.Event;
import baseline.jobShop.simulation.event.JobShop.JobArrivalEvent;
import ec.app.majority.func.E;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class DynamicJobShopSimulation implements Simulation {

    final ArrayList<Job> allJob;
    final ArrayList<Job> waitingJob;
    final Machine[] machines;
    final Queue<Event> eventQueue;
    final int numOfJobs;
    int time = 0;
    DynamicJobShopSimulation(int numOfJobs, int numOfMachines) {
        //start simulation with 10 operations
        allJob = new ArrayList<Job>();
        waitingJob = new ArrayList<Job>();
        eventQueue = new PriorityQueue<Event>();
        machines = populateMachines(numOfMachines);
        this.numOfJobs = numOfJobs;
    }

    public float startSimulation() {

        eventQueue.add(new JobArrivalEvent(time, Job.generateJob(time, machines)));
        //As long as num of jobs has not been satsified keep generating new ones

        //Process events as they come
        while(!eventQueue.isEmpty()) {
            Event nextEvent = eventQueue.poll();
            //Set the time from the event
            time = nextEvent.getTime();
            nextEvent.evalute(this);

            if (numOfJobs > allJob.size()) {
                Job newJob = Job.generateJob(time, machines);
                eventQueue.add(new JobArrivalEvent(time, newJob));
            }
        }

        List<Integer> flowTimes = new ArrayList<Integer>();

        // Calculate mean flow
        for (Job job : allJob) {
            int flowTime = job.getDepartureTime() - job.getArrivalTime();
            flowTimes.add(flowTime);
        }

        //calculate mean flow time
        return (float) flowTimes.stream().mapToInt(Integer::intValue).sum() / flowTimes.size();

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
