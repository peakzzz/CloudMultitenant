package edu.sjsu.projectcloud.dataAccess;

import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskWF;

import java.util.Date;

/**
 * Created by mallika on 4/30/15.
 */
public class WFDAOImpl implements DAO {
    @Override
    public Task getTask() {
        return new TaskWF("Inprogress", "task1", "Dummy Task", new Date(2015, 01, 01), new Date (2015, 12, 31));
    }
}
