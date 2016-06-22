package edu.sjsu.projectcloud.project;

import edu.sjsu.projectcloud.sprint.Sprint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mallika on 5/1/15.
 */
public class ProjectScrum extends Project {

    private List<Sprint> sprints = new ArrayList<>();
    public ProjectScrum(String projecttype, String projectname, Date startdate, Date enddate, String ownername) {
        super(projectname, projecttype, startdate, enddate, ownername);
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }
}
