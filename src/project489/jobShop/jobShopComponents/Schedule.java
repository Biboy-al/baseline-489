package project489.jobShop.jobShopComponents;

import java.lang.Process;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yimei on 22/09/16.
 */
public class Schedule {

    private List<List<java.lang.Process>> processLists;
    List<Job> jobs;

    public Schedule(int numWorkCenters) {
        processLists = new ArrayList<>();
        for (int i = 0; i < numWorkCenters; i++) {
            processLists.add(new ArrayList<>());
        }
        jobs = new ArrayList<>();
    }

    public List<List<java.lang.Process>> getProcessLists() {
        return processLists;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public List<java.lang.Process> getWorkCenterProcessList(int idx) {
        return processLists.get(idx);
    }

    public void add(Process p) {
        processLists.get(p.getWorkCenter().getId()).add(p);
        if (p.getOperation().getNext() == null) {
            // The last operation of the job is finished.
            Job job = p.getOperation().getJob();
            job.setCompletionTime(p.getFinishTime());
            jobs.add(job);
        }
    }

    public double makespan() {
        double value = 0;
        for (Job job : jobs) {
            if (job.getCompletionTime() > value)
                value = job.getCompletionTime();
        }

        return value;
    }

    public double meanFlowtime() {
        double value = 0;
        for (Job job : jobs) {
            value += job.flowTime();
        }

        value /= jobs.size();

        return value;
    }

    public double meanWeightedTardiness() {
        double value = 0;
        for (Job job : jobs) {
            value += job.weightedTardiness();
        }

        value /= jobs.size();

        return value;
    }
}
