package baseline.evaluation;

public abstract  class Event implements Comparable<Event> {

    private double time;
    private int priority;

    public Event(double time, int priority) {
        this.time = time;
        this.priority = priority;
    }

    public double getTime() {
        return time;
    }

    public abstract void evalute(Simulation sim);

    public int getPriority(){
        return priority;
    }

    //If time sheduled is the same base it off priority
    @Override
    public int compareTo(Event o) {
        int comp= Double.compare(time, o.time);
        if (comp == 0){
            return Integer.compare(priority, o.priority);
        }
        return comp;

    }
}
