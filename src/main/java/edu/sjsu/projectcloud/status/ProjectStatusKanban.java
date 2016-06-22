package edu.sjsu.projectcloud.status;

import edu.sjsu.projectcloud.project.ProjectScrum;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mallika on 5/3/15.
 */
public class ProjectStatusKanban extends ProjectStatus {
    private int ready;
    private int inprogress;
    private int complete;
    private int limit = 2;

    public ProjectStatusKanban(int ready, int inprogress, int complete) {
        super();
        this.ready = ready;
        this.inprogress = inprogress;
        this.complete = complete;
    }

    public int getReady() {
        return ready;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public int getInprogress() {
        return inprogress;
    }

    public void setInprogress(int inprogress) {
        this.inprogress = inprogress;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getProjectStatus () {
        String status = "For limit "+limit+ ", the queue over limit is: ";
        if (getReady() > limit) {
            status += "Ready : "+getReady();
        }
        if (getInprogress() > limit) {
            status += ", In Progress : "+getInprogress();
        }
        if (getComplete() > limit) {
            status += ", Complete : "+getComplete();
        }
        return status;
    }
}
