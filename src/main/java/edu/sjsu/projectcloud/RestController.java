package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.db.NullMongoTemplateException;
import edu.sjsu.projectcloud.jtable.JTableResult;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.project.ProjectScrum;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.status.ProjectStatus;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskKanban;
import edu.sjsu.projectcloud.task.TaskScrum;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.task.TaskWF;
import javassist.tools.reflect.Sample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sandy on 5/1/15.
 */
@EnableAutoConfiguration
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/cmpe281project/api")
public class RestController {
    @Autowired
    UserSessionInfo userSessionInfo;

    AppHandler appHandler = new AppHandler();

    private class SampleBean {
        public String user;
        public String pass;

        public SampleBean(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }
    }

    @RequestMapping(value = "/getAllSprintsForProject", method = RequestMethod.GET)
    public List<Sprint> getAllSprintsForProject(@RequestParam(value = "projectId", required = true) String projectId) {
        // get sprints and return the list of sprints here

        List<Sprint> sprints = new ArrayList<>();
//        sprints.add(new Sprint("Sprint-1", "05/02/2015", "05/09/2015"));
//        sprints.add(new Sprint("Sprint-2", "05/10/2015", "05/17/2015"));
        return sprints;
    }

    @RequestMapping(value = "/addStory", method = RequestMethod.POST)
    public String addStory(@RequestBody String requestBody, @RequestParam(required=true, value="projectId") String projectId) {
        // Url will be /cmpe281project/api/addStory?projectId=1
        // RequestBody will be a json with object
        // parse the request body and add it to the project using DAO

        System.out.println("JSON received " + requestBody);


//        DAO dao = DAOFactory.getInstance("SCRUM");
//        dao.addStoryForProject(story, projectId);

        return "success";
    }

    @RequestMapping(value = "/test/dummyproject/{projectid}", method = RequestMethod.GET)
    public Project returnDummyProjectPOJO(@PathVariable String projectid) {
        Project project = setUpDummyProject(projectid);
        return project;
    }

    private Project setUpDummyProject(String projectid) {
        ProjectScrum project = new ProjectScrum("DummyProject", "SCRUM", new Date("05/02/2015"), new Date("05/02/2015"), "Team16");
        project.setId(projectid);
        project.setSprints(setupDummySprints());
        return project;
    }

    private List<Sprint> setupDummySprints() {
        List<Sprint> sprints = new ArrayList<>();
////        Sprint sprint1 = new Sprint("DummySprint1", "05/02/2015", "05/09/2015");
//        sprint1.setId("1234");
//        Sprint sprint2 = new Sprint("DummySprint2", "05/09/2015", "05/16/2015");
//        sprint2.setId("5678");
//        sprint1 = addStories(sprint1);
//        sprint2 = addStories(sprint2);
//        sprints.add(sprint1);
//        sprints.add(sprint2);
        return sprints;
    }

    private Sprint addStories(Sprint sprint) {
        String taskName = sprint.getSprintName()+"DummyTask1";
        TaskScrum taskScrum = new TaskScrum("In progress", taskName, "Dummy Task 1", "XYZ", 50, 10);
        taskScrum.setId("1234");
        sprint.addTask(taskScrum);
        return sprint;
    }

    @RequestMapping(value = "/getProjectInfo/{projectid}", method = RequestMethod.GET)
    public Project getProjectInformation(@PathVariable String projectid) {
        Project project = appHandler.getProject(projectid);
        return project;
    }

    @RequestMapping(value = "/createStory/{projectid}/{sprintid}", method = RequestMethod.POST)
    public Task createScrumTask(@ModelAttribute TaskScrum story, @PathVariable String projectid, @PathVariable String sprintid, Model model) {
        appHandler.addStoryToSprint(story, projectid, sprintid);
        return story;
    }

    @RequestMapping(value = "/deleteStory/{projectid}/{sprintid}/{storyid}", method = RequestMethod.POST)
    public String deleteScrumTask(@PathVariable String projectid, @PathVariable String sprintid, @PathVariable String storyid, Model model) {
        appHandler.deleteStoryFromSprint(projectid, sprintid, storyid);
        return ResponseStatus.class.getSimpleName();
    }

    @RequestMapping(value = "/updateStory/{projectid}/{sprintid}", method = RequestMethod.POST)
    public Task updateScrumTask(@ModelAttribute TaskScrum story, @PathVariable String projectid, @PathVariable String sprintid, Model model) {
        appHandler.updateStoryInSprint(story, projectid, sprintid);
        return story;
    }

    @RequestMapping(value = "/createKanbanTask/{projectid}", method = RequestMethod.POST)
    public Task createTask(@ModelAttribute TaskKanban task, @PathVariable String projectid, Model model) {
        Task dbTask = appHandler.addTaskToProject(task, projectid);
        return dbTask;
    }

    @RequestMapping(value = "/updateKanbanFTask/{projectid}", method = RequestMethod.POST)
    public Task updateKanbanTask(@ModelAttribute TaskWF task, @PathVariable String projectid, Model model) {
        Task dbTask = appHandler.updateTaskInProject(task, projectid);
        return dbTask;
    }

    @RequestMapping(value = "/deleteKanbanTask/{projectid}/{taskid}", method = RequestMethod.POST)
    public String deleteKanbanTask(@PathVariable String projectid, @PathVariable String taskid, Model model) {
        appHandler.deleteTaskFromProject(projectid, taskid);
        return ResponseStatus.class.getSimpleName();
    }

    @RequestMapping(value = "/createWFTask/{projectid}", method = RequestMethod.POST)
    public Task createWFTask(@ModelAttribute TaskWF task, @PathVariable String projectid, Model model) {
        Task dbTask = appHandler.addTaskToProject(task, projectid);
        return dbTask;
    }

    @RequestMapping(value = "/updateWFTask/{projectid}", method = RequestMethod.POST)
    public Task updateWFTask(@ModelAttribute TaskWF task, @PathVariable String projectid, Model model) {
        Task dbTask = appHandler.updateTaskInProject(task, projectid);
        return dbTask;
    }

    @RequestMapping(value = "/deleteWFTask/{projectid}", method = RequestMethod.POST)
    public String createWFTask(@PathVariable String projectid, @PathVariable String taskid, Model model) {
        appHandler.deleteTaskFromProject(projectid, taskid);
        return ResponseStatus.class.getSimpleName();
    }

    @RequestMapping(value = "getAllStories/{projectid}/{sprintid}", method = RequestMethod.GET)
    public List<TaskScrum> getAllStories(@PathVariable String projectid, @PathVariable String sprintid, Model model) {
        List<TaskScrum> stories = appHandler.getAllStoriesForSprint(projectid, sprintid);
        return stories;
    }

    @RequestMapping(value = "getAllSprints/{projectid}", method = RequestMethod.GET)
    public List<Sprint> getAllSprints(@PathVariable String projectid, Model model) {
        //List<Sprint> sprints = appHandler.getAllSprintsForProject(projectid);
        List<Sprint> sprints = null;
        return sprints;
    }


}
