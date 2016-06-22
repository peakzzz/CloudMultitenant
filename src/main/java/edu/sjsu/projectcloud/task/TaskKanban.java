package edu.sjsu.projectcloud.task;

import org.springframework.data.annotation.Id;

/**
 * Created by mallika on 4/30/15.
 */
public class TaskKanban extends Task {

    public TaskKanban(String status, String taskName, String taskDescription, String resourceName) {
        super(status, taskName, taskDescription, resourceName);
    }

    public TaskKanban(String status, String taskName, String taskDescription) {
        super(status, taskName, taskDescription);
    }


}
