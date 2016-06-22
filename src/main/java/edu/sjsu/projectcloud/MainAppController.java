package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.db.NullMongoTemplateException;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.project.ProjectKanban;
import edu.sjsu.projectcloud.project.ProjectScrum;
import edu.sjsu.projectcloud.project.ProjectWF;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.status.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

@Controller
@RequestMapping("/cmpe281project")
@ComponentScan
public class MainAppController {
    @Autowired
    UserSessionInfo userSessionInfo;

    AppHandler appHandler = new AppHandler();

    @RequestMapping(value = "/project/{PID}/status", method = RequestMethod.GET)
    public String viewProjectStatus(@PathVariable("PID") String PID, Model model) {
        ProjectStatus projectStatus = null;
        Project p = new Project();
        p.setId(PID);
        try {
            p = appHandler.getProject(PID);
            projectStatus = appHandler.getProjectStatus(PID);
        } catch (NullMongoTemplateException nmte) {
            return null;
        }
        model.addAttribute("pagetype", "Display Project Status Page");
        model.addAttribute("project", p);
        model.addAttribute("userSessionInfo", userSessionInfo);
        model.addAttribute("projectStatus", projectStatus);
        return "polymorphicView";
       /*
        if (p.getProjecttype().equals("WATERFALL")) {
            return "polymorphic1View";
        } else if (p.getProjecttype().equals("KANBAN")) {
            return "polymorphic3View";
        } else {
            return "polymorphic2View";
        }
        */
    }

    @RequestMapping(value = "/project/{PID}/sprints", method = RequestMethod.GET)
    public String viewSprintList(@PathVariable("PID") String projectId, Model model) {

        AppHandler appHandler = new AppHandler();
        Project project = appHandler.getProject(projectId);
        model.addAttribute("project", project);
        model.addAttribute("userSessionInfo", userSessionInfo);

        return "manageSprintsForProjectScrum";
    }

    @RequestMapping(value = "/project/{PID}/sprint/{SID}", method = RequestMethod.GET)
    public String viewSprint(@PathVariable("PID") String PID, @PathVariable("SID") String SID, Model model) {
        Project p = new Project();
        p.setId(PID);

        Sprint s = new Sprint();
        s.setId(SID);
        model.addAttribute("project", p);
        model.addAttribute("sprint", s);
        model.addAttribute("pagetype", "Display Sprint for given Project Page");
        model.addAttribute("userSessionInfo", userSessionInfo);
        return "polymorphicView";
    }

    @RequestMapping(value = "/project/{PID}", method = RequestMethod.GET)
    public String viewProjectPage(@PathVariable("PID") String PID, Model model) {
        AppHandler handler = new AppHandler();

        Project project = handler.getProject(PID);
        String type = "";

        if (project.getProjecttype().equals("SCRUM")) {
            type = "/sprints";
        } else if (project.getProjecttype().equals("KANBAN")) {
            type = "/cards";
        } else if (project.getProjecttype().equals("WATERFALL")) {
            type = "/tasks";
        }

        return "redirect:/cmpe281project/project/" + PID + type;
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public String viewProjectList(Model model) {
        model.addAttribute("userSessionInfo", userSessionInfo);
        return "manageProjectsForAllType";
    }

    @RequestMapping(value = "/project/{projectId}/sprint/{sprintId}/stories", method = RequestMethod.GET)
    public String manageStoriesForProjectSprint(Model model,
                                                @PathVariable("projectId") String projectId,
                                                @PathVariable("sprintId") String sprintId) {

        AppHandler appHandler = new AppHandler();

        Project project = appHandler.getProject(projectId);
        Sprint sprint = new Sprint(sprintId);

        if (project.getProjecttype().equals("SCRUM")) {
            ProjectScrum ps = (ProjectScrum) project;
            for (Sprint s : ps.getSprints()) {
                if (s.getId().equals(sprintId)) {
                    sprint = s;
                    break;
                }
            }
        }

        addAllModelData(model, project, sprint);
        return "manageStoriesForProjectSprint";
    }

    private void addAllModelData(Model m, Project p, Sprint s) {
        m.addAttribute("project", p);
        m.addAttribute("sprint", s);
        m.addAttribute("userSessionInfo", userSessionInfo);

    }


    @RequestMapping(value = "/project/{projectId}/tasks", method = RequestMethod.GET)
    public String viewTaskslist(Model model, @PathVariable("projectId") String projectId) {
        AppHandler appHandler = new AppHandler();

        Project project = appHandler.getProject(projectId);
        model.addAttribute("userSessionInfo", userSessionInfo);
        model.addAttribute("project", project);
        return "WFJtable";
    }

    @RequestMapping(value = "/project/{projectId}/cards", method = RequestMethod.GET)
    public String viewCardsList(Model model, @PathVariable("projectId") String projectId) {
        AppHandler appHandler = new AppHandler();

        Project project = appHandler.getProject(projectId);
        model.addAttribute("project", project);
        model.addAttribute("userSessionInfo", userSessionInfo);

        return "manageCardsforProjectKanban";
    }

    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public String viewCardsList(Model model) {
        if (userSessionInfo != null) {
            userSessionInfo.setId("");
            userSessionInfo.setUsername("");
            userSessionInfo.setPassword("");
        }
        return "redirect:/cmpe281project";
    }

}
