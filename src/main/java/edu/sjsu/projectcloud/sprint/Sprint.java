package edu.sjsu.projectcloud.sprint;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mallika on 4/30/15.
 */
public class Sprint {
    private String id;
    protected String sprintName;

    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    protected Date endDate;
    protected List<TaskScrum> tasks = new ArrayList<>();

    public Sprint(String sId) {
        this.id = sId;
    }

    public Sprint(String sprintName, Date startDate, Date endDate) {
        this.sprintName = sprintName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Sprint() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TaskScrum> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskScrum> tasks) {
        this.tasks = tasks;
    }

    public void addTask(TaskScrum task) {
        tasks.add(task);
    }

}
