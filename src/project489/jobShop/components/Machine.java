package project489.jobShop.components;

import java.util.ArrayList;
import java.util.List;

public class Machine {

    final private int machineId;
    private final List<Job> waitingJobs;
    private Job currentJob;
    private double totalBusyTime = 0.0;
    private double lastStartTime = 0.0;

    public Machine(int machineId) {
        this.machineId = machineId;
        waitingJobs = new ArrayList<Job>();
    }

    public void setCurrentJob(Job job, double currentTime) {
        if (currentJob != null) {
            // Finish current job: add elapsed time to total busy time
            totalBusyTime += currentTime - lastStartTime;
        }

        this.currentJob = job;

        if (job != null) {
            // Record start time of new job
            lastStartTime = currentTime;
        }
    }

    public Job releaseCurrentJob(double currentTime) {
        if (currentJob != null) {
            totalBusyTime += currentTime - lastStartTime;
        }
        Job finishedJob = this.currentJob;
        this.currentJob = null;
        return finishedJob;
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

    public double getUtilization(double currentTime) {
        double elapsed = currentTime;
        double busy = totalBusyTime;

        if (currentJob != null) {
            busy += currentTime - lastStartTime;
        }

        return (elapsed > 0) ? busy / elapsed : 0.0;
    }


    public boolean isIdle(){
        return currentJob == null;
    }

    public int getMachineId() {
        return machineId;
    }

}
