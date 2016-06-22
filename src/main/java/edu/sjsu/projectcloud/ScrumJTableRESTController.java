package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.jtable.JTableResult;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/cmpe281project/api/scrum")
public class ScrumJTableRESTController {
    @Autowired
    UserSessionInfo userSessionInfo;

    @RequestMapping(value = "/listStories", method = RequestMethod.POST)
    public JTableResult getAllSprintsForJTable(@RequestParam(value = "projectId", required = true) String projectId,
                                               @RequestParam(value = "sprintId", required = true) String sprintId) {
        JTableResult<Task> result = new JTableResult<>(true);
        result.setResult("OK");

        AppHandler appHandler = new AppHandler();
        List<TaskScrum> stories = appHandler.getAllStoriesForSprint(projectId, sprintId);

        for (Task t : stories) {
            result.addRecord(t);
        }
        return result;
    }

    @RequestMapping(value = "/listSprints", method = RequestMethod.POST)
    public JTableResult getAllSprintsForJTable(@RequestParam(value = "projectId", required = true) String projectId) {
        JTableResult<Sprint> result = new JTableResult<>(true);
        result.setResult("OK");

        AppHandler appHandler = new AppHandler();
        List<Sprint> sprints = appHandler.getAllSprintsForProject(projectId);

        for (Sprint t : sprints) {
            result.addRecord(t);
        }
        return result;
    }

    @RequestMapping(value="/createStory", method=RequestMethod.POST)
    public JTableResult<? extends Task> createStory(@ModelAttribute TaskScrum story,
                                                    @RequestParam("projectId") String projectId,
                                                    @RequestParam("sprintId") String sprintId,
                                                    Model model) {
        AppHandler appHandler = new AppHandler();
        story.setId(null);
        appHandler.addStoryToSprint(story, projectId, sprintId);

        JTableResult<TaskScrum> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(story);
        System.out.println(story);
        return result;
    }

    @RequestMapping(value="/createSprint", method=RequestMethod.POST)
    public JTableResult<? extends Sprint> insertSprint(@ModelAttribute Sprint sprint,
                                                       @RequestParam("projectId") String projectId,
                                                       Model model) {
        AppHandler appHandler = new AppHandler();
        sprint.setId(null);
        appHandler.insertSprint(sprint, projectId);
        appHandler.updateProjectAddSprint(projectId, sprint);

        JTableResult<Sprint> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(sprint);
        System.out.println(sprint);
        return result;
    }

    @RequestMapping(value="/updateStory", method=RequestMethod.POST)
    public JTableResult<? extends Task> updateStory(@ModelAttribute TaskScrum story,
                                                    @RequestParam("projectId") String projectId,
                                                    @RequestParam("sprintId") String sprintId,
                                                    Model model) {
        AppHandler appHandler = new AppHandler();
        appHandler.updateStoryInSprint(story, projectId, sprintId);

        JTableResult<TaskScrum> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(story);
        System.out.println(story);
        return result;
    }


    @RequestMapping(value="/updateSprint", method=RequestMethod.POST)
    public JTableResult<? extends Sprint> updateSprint(@ModelAttribute Sprint sprint,
                                                    @RequestParam("projectId") String projectId,
                                                    Model model) {
        AppHandler appHandler = new AppHandler();
        appHandler.updateProjectAddSprint( projectId, sprint );

        JTableResult<Sprint> result = new JTableResult<>();
        result.setResult("OK");
        result.addRecord(sprint);
        System.out.println(sprint);
        return  result;
    }
    
    @RequestMapping(value="/deleteStory", method=RequestMethod.POST)
    public JTableResult<? extends Task> deleteStory(@ModelAttribute TaskScrum story,
                                                    @RequestParam("projectId") String projectId,
                                                    @RequestParam("sprintId") String sprintId,
                                                    Model model) {
        AppHandler appHandler = new AppHandler();
        appHandler.deleteStoryFromSprint(projectId, sprintId, story.getId());
        return new JTableResult<>();
    }
}

