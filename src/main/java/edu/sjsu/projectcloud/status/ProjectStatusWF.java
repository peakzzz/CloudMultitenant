package edu.sjsu.projectcloud.status;

import edu.sjsu.projectcloud.project.ProjectScrum;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mallika on 5/3/15.
 */
public class ProjectStatusWF extends ProjectStatus {
    private int percentageComplete;

    public ProjectStatusWF(int percentageComplete) {
        super();
        this.percentageComplete = percentageComplete;
    }

    public int getPercentageComplete() {
        return percentageComplete;
    }

    public void setPercentageComplete(int percentageComplete) {
        this.percentageComplete = percentageComplete;
    }
}
