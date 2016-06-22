package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.project.ProjectWF;
import edu.sjsu.projectcloud.task.TaskKanban;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by mallika on 5/2/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloApp.class)
@WebAppConfiguration
public class AppHandlerTest {

    @Test
    public void testAddStoryToSprint() throws Exception {
        AppHandler appHandler = new AppHandler();
        String taskName = "DummyTask2";
        String projectid = "5545c12677c8a107cae8903f";
        String sprintid = "5545c13877c8a107cae89040";
        TaskScrum taskScrum = new TaskScrum("In progress", taskName, "Dummy Task 2", "ABC", 50, 10);
        appHandler.addStoryToSprint(taskScrum, projectid, sprintid);
    }


    @Test
    public void testDeleteStoryFromSprint() throws Exception {
        AppHandler appHandler = new AppHandler();
        String projectid = "5545c12677c8a107cae8903f";
        String sprintid = "5545c13877c8a107cae89040";
        String storyid = "5545c22577c8ebe9f027f4c5";
        appHandler.deleteStoryFromSprint(projectid, sprintid, storyid);
    }

    @Test
    public void testAddTaskKanbanToProject() throws Exception {
        AppHandler appHandler = new AppHandler();
        String projectid = "5545d26177c8dd1924c84783";
        String taskName = "DummyKanban2s";
        TaskKanban taskKanban = new TaskKanban("In progress", taskName, "Dummy Task 2 Kanban", "ABC");
        appHandler.addTaskToProject(taskKanban, projectid);
    }

    @Test
    public void testDeleteTaskFromProject() throws Exception {
        AppHandler appHandler = new AppHandler();
        String projectid = "5545d26177c8dd1924c84783";
        String taskid = "554650dd77c86cc4fc3bfca1";
        appHandler.deleteTaskFromProject(projectid, taskid);
    }

    @Test
    public void testInsertProjectAndAddToResource() throws Exception {
        Project project = new ProjectWF("KANBAN", "TESTKANBAN1", new Date("05/02/2015"), new Date("05/02/2015"), "m");
        String userid = "5545c11977c8a107cae8903e";
        AppHandler appHandler = new AppHandler();
        appHandler.insertProjectAndAddToResource(userid, project);
    }

    @Test
    public void testUpdateTaskInProject() throws Exception {
        TaskKanban taskKanban = new TaskKanban("In progress", "DummyKanbanUpdated", "Dummy Task 2 Kanban Updated", "ABC");
        taskKanban.setId("5546564777c851169492287d");
        AppHandler appHandler = new AppHandler();
        appHandler.updateTaskInProject(taskKanban, "5545d26177c8dd1924c84783");
    }
}