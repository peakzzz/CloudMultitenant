package edu.sjsu.projectcloud.db;

import edu.sjsu.projectcloud.HelloApp;
import edu.sjsu.projectcloud.RestController;
import edu.sjsu.projectcloud.project.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by mallika on 5/1/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ResourceAccessTest {

    @Test
    public void testAddProject() {
        ResourceAccess resourceAccess = new ResourceAccess();
        Project project = new Project("SCRUM", "P1", new Date("05/02/2015"),new Date("05/02/2015"), "m");
        try {
            resourceAccess.updateResourceAddProject("m", project);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Oops");
        }
    }

}