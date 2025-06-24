package baseline.jobShop.simulation.event;

import baseline.jobShop.simulation.Simulation;

public abstract  class Event implements Comparable<Event> {

    private int time;
    private int priority;

    public Event(int time, int priority) {
        this.time = time;
        this.priority = priority;
    }

    public int getTime() {
        return time;
    }

    public abstract void evalute(Simulation sim);

    public int getPriority(){
        return priority;
    }

    //If time sheduled is the same base it off priority
    @Override
    public int compareTo(Event o) {
        int comp= Integer.compare(time, o.time);
        if (comp == 0){
            return Integer.compare(priority, o.priority);
        }
        return comp;

    }
}
