package edu.sjsu.projectcloud.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

/**
 * Created by mallika on 4/30/15.
 */
public class Task {
    @Id
    protected String id;

    protected String status;
    protected String taskName;
    protected String taskDescription;
    protected String resourceName;

    public Task(String status, String taskName, String taskDescription, String resourceName) {
        this.status = status;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.resourceName = resourceName;
    }

    public Task(String status, String taskName, String taskDescription) {
        this(status, taskName, taskDescription, "");
    }

    public Task() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
