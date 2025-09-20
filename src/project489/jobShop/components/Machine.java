package project489.jobShop.components;

import java.util.ArrayList;
import java.util.List;

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

    public void reset(){
        waitingJobs.clear();
        currentJob = null;
    }

    public void addJob(Job job) {

        if (job.isFinished()) return;

        if (!waitingJobs.contains(job)) {
            waitingJobs.add(job);
        }
    }

    public List<Job> getWaitingJobs() {
        return waitingJobs;
    }

    public void removeJob(Job job) {
        waitingJobs.remove(job);
    }


    public boolean isIdle(){
        return currentJob == null;
    }

    public int getMachineId() {
        return machineId;
    }

}
