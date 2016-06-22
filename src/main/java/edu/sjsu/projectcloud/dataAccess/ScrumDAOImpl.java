package edu.sjsu.projectcloud.dataAccess;

import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import edu.sjsu.projectcloud.task.TaskWF;

import java.util.Date;

/**
 * Created by mallika on 4/30/15.
 */
public class ScrumDAOImpl implements DAO {
    @Override
    public Task getTask() {
        return new TaskScrum("Inprogress", "task1", "Dummy Task", 50, 10);
    }
}
