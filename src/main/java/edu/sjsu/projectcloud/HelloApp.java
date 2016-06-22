package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.dataAccess.DAO;
import edu.sjsu.projectcloud.dataAccess.DAOFactory;
import edu.sjsu.projectcloud.db.NullMongoTemplateException;
import edu.sjsu.projectcloud.db.ProjectAccess;
import edu.sjsu.projectcloud.db.ResourceAccess;
import edu.sjsu.projectcloud.exceptions.InvalidLoginException;
import edu.sjsu.projectcloud.exceptions.NoSuchUserException;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.session.User;
import edu.sjsu.projectcloud.session.UserSessionInfo;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.status.ProjectStatus;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EnableAutoConfiguration
@Controller
@RequestMapping("/cmpe281project")
@ComponentScan
@EnableWebMvc
@Configuration
public class HelloApp extends WebMvcConfigurerAdapter {
    @Autowired
    UserSessionInfo userSessionInfo;

    AppHandler appHandler = new AppHandler();

    public static void main(String... args) {
        SpringApplication.run(HelloApp.class);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getIndexPage(Model model) {
        if (userSessionInfo.getUsername().isEmpty() && userSessionInfo.getPassword().isEmpty()) {
            model.addAttribute("error", false);
            return "index";
        }
        try {
            model.addAttribute("error", true);
            User u = appHandler.validateAndGetUser(userSessionInfo.getUsername(), userSessionInfo.getPassword());
        } catch(InvalidLoginException e) {
            model.addAttribute("message", "Please log in to continue!");
            return "index";
        } catch(NoSuchUserException e) {
            model.addAttribute("message", "Username or password does not exist!");
            return "index";
        }
        return "redirect:/cmpe281project/projects";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getUserRegistrationPage(Model model) {
        Resource resource = new Resource();
        model.addAttribute("resource", resource);
        return "register";
    }

    @RequestMapping(value = "/register/users", method = RequestMethod.POST)
    public String createResource(@ModelAttribute Resource resource, Model model) {
        if (appHandler.doesUserExist(resource.getUsername()) != null) {
            return "userexists";
        }
        appHandler.insertResource(resource);
        String username = resource.getUsername();
        Project project = new Project();
        model.addAttribute("project", project);

        userSessionInfo.setUsername(resource.getUsername());
        userSessionInfo.setPassword(resource.getPassword());
        userSessionInfo.setId(resource.getId());

        model.addAttribute("userSessionInfo", userSessionInfo);
        return "redirect:/cmpe281project/projects";
    }

    @RequestMapping(value = "/index/users", method = RequestMethod.POST)
    public String getResource(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            User u = appHandler.validateAndGetUser(username, password);
            userSessionInfo.setUsername(u.getUsername());
            userSessionInfo.setPassword(u.getPassword());
            userSessionInfo.setId(u.getId());
        } catch(InvalidLoginException e) {
            model.addAttribute("error", true);
            return "index";
        } catch(NoSuchUserException e) {
            model.addAttribute("error", true);
            return "index";
        }

        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("userSessionInfo", userSessionInfo);
        return "redirect:/cmpe281project/projects";
    }

    private void getAllResources(ResourceAccess resourceAccess) {
        List<Resource> resources = resourceAccess.getAllResources();
        for (Resource r : resources) {
            System.out.println(r.toString());
        }
    }

    private DAO getDAO(String type) {
        return DAOFactory.getInstance(type);
    }

    @RequestMapping(value = "/test/TaskScrumPage", method = RequestMethod.GET)
    public String showMockTaskScrumPage(@ModelAttribute Project project, @ModelAttribute String username, Model model) {
        // This is dummy, replace with access to DAO
        model.addAttribute("task", new TaskScrum("test", "test", "test", 1, 1));
        return "TaskScrum";
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST)
    public String createProject(@ModelAttribute Project project, Model model) {
        if (appHandler.doesProjectExistInResource(project.getOwnername(), project) != null) {
            return "login";
        }
        Project dbProject = appHandler.insertProject(project);
        appHandler.updateResourceAddProject(dbProject.getOwnername(), dbProject);
        DAO dao = this.getDAO(project.getProjecttype());
        Task task1 = dao.getTask();
        model.addAttribute("project", dbProject);
        model.addAttribute("username", project.getOwnername());
        model.addAttribute("userSessionInfo", userSessionInfo);

        if (project.getProjecttype().equals("SCRUM")) {
            Sprint sprint = new Sprint();
            model.addAttribute(sprint);
            return "createsprint";
        } else {
            model.addAttribute("task", task1);
            return task1.getClass().getSimpleName();
        }
    }



    @RequestMapping(value = "/sprint/{username}/{projectid}", method = RequestMethod.POST)
    public String createSprint(@ModelAttribute Sprint sprint, @PathVariable String username, @PathVariable String projectid, Model model) {
        Task taskScrum = new TaskScrum();
        Sprint dbSprint = appHandler.insertSprint(sprint, projectid);
        appHandler.updateProjectAddSprint(projectid, sprint);
        model.addAttribute("task", taskScrum);
        model.addAttribute("username", username);
        model.addAttribute("projectid", projectid);
        model.addAttribute("sprintName", sprint.getSprintName());
        model.addAttribute("userSessionInfo", userSessionInfo);
        return "TaskScrum";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            System.out.println(url.getFile());
        }

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/js/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }



    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("MM/dd/yyyy"));
        return builder;
    }
}

