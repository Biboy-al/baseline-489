package baseline.jobShop.simulation.event;

import baseline.jobShop.components.Job;
import baseline.jobShop.components.Machine;
import baseline.jobShop.simulation.DynamicJobShopSimulation;
import baseline.evaluation.Simulation;
import baseline.evaluation.Event;

public class OperationStartEvent extends Event {

    Job job;
    Machine machine;
    public OperationStartEvent(double time, Machine machine, Job job) {
        super(time, 2);
        this.machine = machine;
        this.job = job;
    }

    @Override
    public void evalute(Simulation sim) {
        DynamicJobShopSimulation jobShopSim = (DynamicJobShopSimulation) sim;
        machine.setCurrentJob(this.job);
        double processingTime = job.getCurrentOperation().getProcessingTime();
        jobShopSim.addEvent(new OperationEndEvent(processingTime + super.getTime(), machine, job));
    }

}
