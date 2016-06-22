package edu.sjsu.projectcloud.task;

/**
 * Created by mallika on 4/30/15.
 */
public class TaskScrum extends Task {

    protected int hoursAllotted;
    protected int hoursCompleted;

    public TaskScrum(String status, String taskName, String taskDescription, String resourceName, int hoursAllotted, int hoursCompleted) {
        super(status, taskName, taskDescription, resourceName);
        this.hoursAllotted = hoursAllotted;
        this.hoursCompleted = hoursCompleted;
    }

    public TaskScrum(String status, String taskName, String taskDescription, int hoursAllotted, int hoursCompleted) {
        super(status, taskName, taskDescription);
        this.hoursAllotted = hoursAllotted;
        this.hoursCompleted = hoursCompleted;
    }



    public TaskScrum() {}

    public int getHoursAllotted() {
        return hoursAllotted;
    }

    public void setHoursAllotted(int hoursAllotted) {
        this.hoursAllotted = hoursAllotted;
    }

    public int getHoursCompleted() {
        return hoursCompleted;
    }

    public void setHoursCompleted(int hoursCompleted) {
        this.hoursCompleted = hoursCompleted;
    }

    @Override
    public String toString() {
        return "TaskScrum{" +
                "hoursAllotted=" + hoursAllotted +
                ", hoursCompleted=" + hoursCompleted +
                '}';
    }
}
