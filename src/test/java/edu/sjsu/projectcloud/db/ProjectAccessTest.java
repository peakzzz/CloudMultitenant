package edu.sjsu.projectcloud.db;

import edu.sjsu.projectcloud.HelloApp;
import edu.sjsu.projectcloud.MainAppController;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Created by mallika on 5/2/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HelloApp.class)
@WebAppConfiguration
public class ProjectAccessTest {

    @Test
    public void testUpdateProjectAddStoryToSprint() throws Exception {
        String taskName = "DummyTask1";
        String projectid = "5545686177c8195d97d4d6f5";
        String sprintid = "5545687877c8195d97d4d6f6";
        TaskScrum taskScrum = new TaskScrum("In progress", taskName, "Dummy Task 1", "XYZ", 50, 10);
        SprintAccess sprintAccess = new SprintAccess();
        ProjectAccess projectAccess = new ProjectAccess();
        Sprint sprint = sprintAccess.updateSprintAddTask(sprintid, taskScrum);
        //projectAccess.updateProjectAddStoryToSprint(projectid, sprint, taskScrum);
    }
}