package edu.sjsu.projectcloud.project;

import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.task.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mallika on 4/30/15.
 */
public class ProjectWF extends Project {

    public ProjectWF(String projecttype, String projectname, Date startdate, Date enddate, String ownername) {
        super(projectname, projecttype, startdate, enddate, ownername);
    }
}
