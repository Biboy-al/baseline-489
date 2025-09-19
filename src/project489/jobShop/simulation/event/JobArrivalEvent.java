package project489.jobShop.simulation.event;

import project489.jobShop.components.Job;
import project489.jobShop.components.Machine;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.evaluation.Simulation;
import project489.evaluation.Event;

public class JobArrivalEvent extends Event {

    private Job job;

    public JobArrivalEvent(double time, Job job) {
        super(time,3);
        this.job = job;
    }

    @Override
    public void evalute(Simulation sim) {
        DynamicJobShopSimulation jobShopSim = (DynamicJobShopSimulation) sim;
        Machine machine = jobShopSim.getMachine(this.job.getCurrentOperation().getMachineId());

        //Makes sure that only new operations are added
        if (job.getCurrentOpeationIndex() <= 0) {
            //Add job to all jobs
            jobShopSim.addJob(this.job);
        }

        //If machine is idle start the operation
        if (machine.isIdle()){
            jobShopSim.addEvent(new OperationStartEvent(super.getTime(), machine, this.job));
        }else{
            //If not, add job to the queue
            //and track what is the current time
            this.job.setArrivalInQueueTime(this.getTime());
            machine.addJob(this.job);
        }

    }

    @Override
    public int getPriority() {
        return 3;
    }
}