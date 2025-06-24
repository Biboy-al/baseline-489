package baseline.jobShop.simulation.event.JobShop;

import baseline.jobShop.components.Job;
import baseline.jobShop.components.Machine;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import baseline.jobShop.simulation.Simulation;
import baseline.jobShop.simulation.event.Event;

public class OperationEndEvent extends Event {

    Machine machine;
    Job job;
    public OperationEndEvent(int time, Machine machine, Job job) {
        super(time, 1);
        this.machine = machine;
        this.job = job;
    }

    @Override
    public void evalute(Simulation sim) {
        DynamicJobShopSimulation jobShopSim = (DynamicJobShopSimulation) sim;
        this.machine.releaseCurrentJob();
        this.job.finishCurrentOperation();

        //if current job is not finished
        //reintegrate the new job into the event lifecycle
        if(!this.job.isFinished()){
            jobShopSim.addEvent(new JobArrivalEvent(super.getTime(), this.job));
        }else{
            //if it is finished, set departure time
            //And break from event loop
            this.job.setDepartureTime(super.getTime());
        }
        if(!machine.getWaitingJobs().isEmpty()){
            Job nextJob = machine.getWaitingJobs().remove(0);
            jobShopSim.addEvent(new OperationStartEvent(super.getTime(), this.machine,nextJob));
        }

    }

    @Override
    public int getPriority() {
        return 1;
    }
}

