package edu.sjsu.projectcloud.status;

import edu.sjsu.projectcloud.project.ProjectScrum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mallika on 5/3/15.
 */
public class ProjectStatusScrum extends ProjectStatus {
    private String completionDate;
    private int hoursRemaining;

    public ProjectStatusScrum(int hoursRemaining) {
        super();
        this.hoursRemaining = hoursRemaining;
    }

    public ProjectStatusScrum() {
    }

    private String calculateCompletionDate() {
        int daysRemaining = (hoursRemaining/8);
        if(hoursRemaining%8 > 0) {
            daysRemaining++;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, daysRemaining);
        String output = sdf.format(c.getTime());
        return output;
    }

    public String getCompletionDate() {
        completionDate = calculateCompletionDate();
        return completionDate;
    }

    public void setCompletionDate(String completionDate) {
        this.completionDate = completionDate;
    }
}
