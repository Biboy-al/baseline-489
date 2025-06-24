package baseline.jobShop.components;

import baseline.jobShop.components.Job;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

public class Machine {

    final private int machineId;
    private final List<Job> waitingJobs;
    private Job currentJob;

    public Machine(int machineId) {
        this.machineId = machineId;
        waitingJobs = new ArrayList<Job>();
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public Job releaseCurrentJob() {
        Job currentJob = this.currentJob;
        this.currentJob = null;
        return currentJob;
    }

    public void addJob(Job job) {
        waitingJobs.add(job);
    }

    public List<Job> getWaitingJobs() {
        return waitingJobs;
    }

//    public void processNextJob() {
//        //for now first in and first out
//        if(currentJob == null && !waitingJobs.isEmpty()) {
//            currentJob = waitingJobs.remove(0);;
//        }
//    }

    public boolean isIdle(){
        return currentJob == null;
    }

    public int getMachineId() {
        return machineId;
    }

}
