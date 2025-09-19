package project489.jobShop.simulation.event;

import project489.jobShop.components.Job;
import project489.jobShop.components.Machine;
import project489.jobShop.simulation.DynamicJobShopSimulation;
import project489.evaluation.Simulation;
import project489.evaluation.Event;

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
