package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.jtable.JTableResult;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/cmpe281project/api/projects")
public class ProjectJTableRESTController {
    @Autowired
    UserSessionInfo userSessionInfo;

    @RequestMapping(value = "/listProjects", method = RequestMethod.POST)
    public JTableResult getAllProjectForJTable() {
        JTableResult<Project> result = new JTableResult<>(true);
        result.setResult("OK");

        AppHandler appHandler = new AppHandler();
        List<Project> projects = appHandler.getAllProjectsPerResource(userSessionInfo.getId());

        for (Project t : projects) {
            result.addRecord(t);
        }
        return result;
    }

    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    public JTableResult<? extends Project> createProject(@ModelAttribute Project project) {
        AppHandler appHandler = new AppHandler();
        project.setId(null);
        project.setOwnername(userSessionInfo.getUsername());
        appHandler.insertProjectAndAddToResource(userSessionInfo.getId(), project);

        JTableResult<Project> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(project);
        System.out.println(project);
        return result;
    }

}