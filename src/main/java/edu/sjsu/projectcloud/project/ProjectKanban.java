package edu.sjsu.projectcloud.project;

import java.util.Date;

/**
 * Created by mallika on 4/30/15.
 */
public class ProjectKanban extends Project {

    public ProjectKanban(String projecttype, String projectname, Date startdate, Date enddate, String ownername) {
        super(projectname, projecttype, startdate, enddate, ownername);
    }
}
