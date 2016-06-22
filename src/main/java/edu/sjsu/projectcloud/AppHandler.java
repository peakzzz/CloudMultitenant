package edu.sjsu.projectcloud;

import edu.sjsu.projectcloud.db.*;
import edu.sjsu.projectcloud.exceptions.InvalidLoginException;
import edu.sjsu.projectcloud.exceptions.NoSuchUserException;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.session.User;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.status.*;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskKanban;
import edu.sjsu.projectcloud.task.TaskScrum;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


/**
 * Created by mallika on 5/1/15.
 */
public class AppHandler {
    ResourceAccess resourceAccess = new ResourceAccess();
    ProjectAccess projectAccess = new ProjectAccess();
    SprintAccess sprintAccess = new SprintAccess();
    TaskAccess taskAccess = new TaskAccess();

    public User validateAndGetUser(String username, String password) throws NoSuchUserException, InvalidLoginException {
        User u = new User();
        try {
            Resource r = resourceAccess.findResource(username);

            if (r == null) {
                throw new NoSuchUserException();
            }

            if (validateUsernamePassword(username, password)) {
                u = new User(r);
                return u;
            } else {
                throw new InvalidLoginException();
            }
        } catch (NullMongoTemplateException e) {
            e.printStackTrace();
        }

        return u;
    }

    public Resource doesUserExist(String username) {
        Resource resource;
        try {
            resource = resourceAccess.findResource(username);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return resource;
    }

    public Project getProject(String projectid) {
        Project project = null;
        try {
            project = projectAccess.getProject(projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return project;
    }

    public void insertResource(Resource resource) {
        resourceAccess.insertResource(resource);
    }

    public Project insertProject(Project project) {
        Project dbProject = projectAccess.insertProject(project);
        return dbProject;
    }

    public boolean validateUsernamePassword(String username, String password) {
        Resource resource = doesUserExist(username);
        if (resource.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public Project doesProjectExistInResource(String username, Project project) {
        Project project1;
        try {

            project1 = resourceAccess.findProjectinResource(username, project.getProjectname(), project.getProjecttype());
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return project1;

    }

    public boolean updateResourceAddProject(String username, Project project) {
        try {
            resourceAccess.updateResourceAddProject(username, project);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return false;
        }
        return true;
    }

    public Sprint doesSprintExistInProject(String projectname, String projecttype, String username, Sprint sprint)  {
        Sprint newSprint;
        try {
            newSprint = projectAccess.findSprintinProject(username, projectname, projecttype, sprint.getSprintName());
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo connection not established");
            return null;
        }
        return newSprint;
    }

    public Sprint insertSprint(Sprint sprint, String projectid) {
        Sprint dbSprint = sprintAccess.insertSprint(sprint);
        return dbSprint;
    }

    public boolean updateProjectAddSprint(String projectid, Sprint sprint) {
        try {
            projectAccess.updateProjectAddSprint(projectid, sprint);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return false;
        }
        return true;
    }

    public Task addStoryToSprint(TaskScrum story, String projectid, String sprintid) {
        TaskScrum dbTask = (TaskScrum) taskAccess.insertTask(story);
        try {
            Sprint sprint = sprintAccess.updateSprintAddTask(sprintid, dbTask);
            projectAccess.updateProjectUpdateStoryInSprint(projectid, sprint, story);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return dbTask;
    }

    public Task updateStoryInSprint(TaskScrum story, String projectid, String sprintid) {
        Task dbTask = taskAccess.updateTask(story);
        try {
            Sprint sprint = sprintAccess.updateSprintUpdateTask(sprintid, story);
            projectAccess.updateProjectUpdateStoryInSprint(projectid, sprint, story);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return dbTask;
    }

    public void deleteStoryFromSprint(String projectid, String sprintid, String storyid) {
        try {
            taskAccess.deleteTask(storyid);
            Sprint sprint = sprintAccess.updateSprintDeleteStory(sprintid, storyid);
            projectAccess.updateProjectDeleteStoryFromSprint(projectid, sprint, storyid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
    }

    public Task addTaskToProject(Task task, String projectid) {
        Task dbTask = taskAccess.insertTask(task);
        try {
            projectAccess.updateProjectAddTaskToProject(task, projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return dbTask;
    }

    public Task updateTaskInProject(Task task, String projectid) {
        Task dbTask = taskAccess.updateTask(task);
        try {
            projectAccess.updateProjectUpdateTaskInProject(task, projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
            return null;
        }
        return dbTask;
    }

    public void deleteTaskFromProject(String projectid, String taskid) {
        try {
            taskAccess.deleteTask(taskid);
            projectAccess.deleteTaskFromProject(projectid, taskid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
    }

    public List<TaskScrum> getAllStoriesForSprint(String projectid, String sprintid) {
        List<TaskScrum> stories = new ArrayList<>();
        try {
           stories = sprintAccess.getStories(sprintid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return stories;
    }


    public List<Sprint> getAllSprintsForProject(String projectid) {
        List<Sprint> sprints = new ArrayList<>();
        try {
            sprints = projectAccess.getSprints(projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return sprints;
    }

    public List<Task> getAllTasks(String projectid) {
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = projectAccess.getTasks(projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return tasks;
    }

    public List<Project> getAllProjectsPerResource(String userid) {
        List<Project> projects = new ArrayList<>();
        try {
            projects = resourceAccess.getProjects(userid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return projects;
    }

    public Project insertProjectAndAddToResource(String userid, Project project) {
        try {
            Project dbProject = projectAccess.insertProject(project);
            resourceAccess.addProjectToResource(project, userid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return project;
    }

    public ProjectStatus getProjectStatus(String projectid) throws NullMongoTemplateException {
        String projecttype = "";
        try {
            projecttype = projectAccess.getProjecttype(projectid);
        } catch (NullMongoTemplateException nmte) {
            System.out.println("Mongo Connection failed");
        }
        return getProjectStatus(projectid, projecttype);
    }

    private ProjectStatus getProjectStatus(String projectid, String projecttype) {
        ProjectStatus projectStatus = null;
        if (projecttype.equals("SCRUM")) {
            int hoursRemaining = 0;
            try {
                hoursRemaining = projectAccess.getHoursRemaining(projectid, projecttype);
            } catch (NullMongoTemplateException nmte) {
                System.out.println("Mongo Connection failed");
            }
            projectStatus = new ProjectStatusScrum(hoursRemaining);
        } else if (projecttype.equals("WATERFALL")) {
            int percentageComplete = 0;
            try {
                percentageComplete = projectAccess.getPercentageComplete(projectid, projecttype);
            } catch (NullMongoTemplateException nmte) {
                System.out.println("Mongo Connection failed");
            }
            projectStatus = new ProjectStatusWF(percentageComplete);
        } else if (projecttype.equals("KANBAN")) {
            Map<String, Integer> table = new Hashtable<>();
            try {
                table = projectAccess.getQueuesOverLimit(projectid, projecttype);
            } catch (NullMongoTemplateException nmte) {
                System.out.println("Mongo Connection failed");
            }
            projectStatus = new ProjectStatusKanban(table.get("Ready"), table.get("Inprogress"), table.get("Completed"));
        }

        return projectStatus;
    }

}
