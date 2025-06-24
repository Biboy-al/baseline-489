package baseline.jobShop.simulation.event.JobShop;

import baseline.jobShop.components.Job;
import baseline.jobShop.components.Machine;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import baseline.jobShop.simulation.Simulation;
import baseline.jobShop.simulation.event.Event;
import baseline.jobShop.simulation.event.JobShop.OperationStartEvent;

public class JobArrivalEvent extends Event {

    private Job job;

    public JobArrivalEvent(int time, Job job) {
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
            machine.addJob(this.job);
        }

    }

    @Override
    public int getPriority() {
        return 3;
    }
}