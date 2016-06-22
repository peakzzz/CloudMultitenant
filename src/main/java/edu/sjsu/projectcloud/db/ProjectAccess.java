package edu.sjsu.projectcloud.db;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.project.ProjectKanban;
import edu.sjsu.projectcloud.project.ProjectScrum;
import edu.sjsu.projectcloud.project.ProjectWF;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.status.ProjectStatus;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskKanban;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by mallika on 5/1/15.
 */
public class ProjectAccess {

    private MongoOperations getMongoOperations() throws UnknownHostException {
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "test"));
        return mongoOperations;
    }

    public Project insertProject(Project project) {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            mongoOperations.insert(project, "project");
        }
        return project;
    }

    public Sprint findSprintinProject(String username, String projectName, String projectType, String sprintName) throws  NullMongoTemplateException{
        Sprint sprint = null;
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        ProjectScrum project = mongoOperations.findOne(query(where("projectname").is(projectName).and("ownername").is(username).and("projecttype").is(projectType)), ProjectScrum.class);
        if(project != null){
            List<Sprint> sprints = project.getSprints();
            if (sprints.size() == 0) {
                return null;
            }
            for (Sprint sprint1 : sprints) {
                if (sprint1.getSprintName().equals(sprintName)) {
                    sprint = sprint1;
                    break;
                }
            }
        }
        return sprint;
    }

    public void updateProjectAddTask(String projectname, String username, Task task) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        mongoOperations.updateFirst(query(where("projectname").is(projectname).and("ownername").is(username)), new Update().push("tasks", task), Resource.class);
    }

    public void updateProjectAddSprint(String projectid, Sprint sprint) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        mongoOperations.updateFirst(query(where("_id").is(projectid)), new Update().push("sprints", sprint), Project.class);
    }

    public Project getProject(String projectid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Project project = mongoOperations.findOne(query(where("_id").is(projectid)), Project.class);

        if (project.getProjecttype().equals("SCRUM")) {
            ProjectScrum ps = mongoOperations.findOne(query(where("_id").is(projectid)), ProjectScrum.class, "project");
            project = ps;
        }
        return project;
    }

    private MongoOperations getMongoOperationInstance() {
        MongoOperations mongoOperations = null;
        try {
            mongoOperations = getMongoOperations();
        } catch (UnknownHostException uhe) {
            System.out.println("Unknown Host");
            return null;
        }
        return mongoOperations;
    }

    public void updateProjectUpdateStoryInSprint(String projectid, Sprint sprint, Task task) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(projectid));
        Update update = new Update().pull("sprints", new BasicDBObject("_id", sprint.getId()));
        mongoOperations.updateFirst(query, update, Project.class);
        mongoOperations.updateFirst(query(where("_id").is(projectid)), new Update().push("sprints", sprint), Project.class);
    }

    public void updateProjectDeleteStoryFromSprint(String projectid, Sprint sprint, String taskScrumid) throws NullMongoTemplateException{
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(projectid));
        Update update = new Update().pull("sprints", new BasicDBObject("_id", sprint.getId()));
        mongoOperations.updateFirst(query, update, Project.class);
        mongoOperations.updateFirst(query(where("_id").is(projectid)), new Update().push("sprints", sprint), Project.class);
    }

    public void updateProjectAddTaskToProject(Task task, String projectid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(projectid));
        Update update = new Update().push("tasks", task);
        mongoOperations.updateFirst(query, update, Project.class);
    }

    public void updateProjectUpdateTaskInProject(Task task, String projectid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(projectid));
        Update update = new Update().pull("tasks", new BasicDBObject("_id", task.getId()));
        mongoOperations.updateFirst(query, update, Project.class);
        update = new Update().push("tasks", task);
        mongoOperations.updateFirst(query, update, Project.class);
    }

    public void deleteTaskFromProject(String projectid, String taskid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(projectid));
        Update update = new Update().pull("tasks", new BasicDBObject("_id", taskid));
        mongoOperations.updateFirst(query, update, Project.class);
    }

    public List<Sprint> getSprints(String projectid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        ProjectScrum project = mongoOperations.findOne(query(where("_id").is(projectid)), ProjectScrum.class, "project");
        List<Sprint> sprints = project.getSprints();
        return sprints;
    }

    public List<Task> getTasks(String projectid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Project project = mongoOperations.findOne(query(where("_id").is(projectid)), Project.class);
        List<Task> tasks = project.getTasks();
        return tasks;
    }

    public String getProjecttype(String projectid) throws  NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Project project = mongoOperations.findOne(query(where("_id").is(projectid)), Project.class);
        String projectType = project.getProjecttype();
        return projectType;
    }

    public int getHoursRemaining(String projectid, String projecttype) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        ProjectScrum project = mongoOperations.findOne(query(where("_id").is(projectid)), ProjectScrum.class, "project");
        int totalHours = 0;
        int hoursCompleted = 0;
        List<Sprint> sprints = project.getSprints();
        for (Sprint sprint : sprints) {
            List<TaskScrum> tasks = sprint.getTasks();
            for (TaskScrum task : tasks) {
                totalHours = totalHours + task.getHoursAllotted();
                hoursCompleted = hoursCompleted + task.getHoursCompleted();
            }
        }
        int hoursRemaining = totalHours - hoursCompleted;
        return hoursRemaining;
    }

    public int getPercentageComplete(String projectid, String projecttype) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        ProjectKanban project = mongoOperations.findOne(query(where("_id").is(projectid)), ProjectKanban.class, "project");
        List<Task> tasks = project.getTasks();
        int countReady = 0;
        int countInProgress = 0;
        int countComplete = 0;
        for (Task task : tasks) {
            if (task.getStatus().equals("Ready")) {
                countReady++;
            } else if (task.getStatus().equals("Inprogress")) {
                countInProgress++;
            } else {
                countComplete++;
            }
        }
        int totalCount = countComplete + countReady + countInProgress;
        System.out.println(countComplete + countReady + countInProgress);
        int percentageComplete = (1/3)*100;
        return percentageComplete;
    }

    public Map<String, Integer> getQueuesOverLimit(String projectid, String projecttype) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        ProjectWF project = mongoOperations.findOne(query(where("_id").is(projectid)), ProjectWF.class, "project");
        List<Task> tasks = project.getTasks();
        int countReady = 0;
        int countInProgress = 0;
        int countComplete = 0;
        for (Task task : tasks) {
            if (task.getStatus().equals("1")) {
                countReady++;
            } else if (task.getStatus().equals("2")) {
                countInProgress++;
            } else {
                countComplete++;
            }
        }
        Map<String, Integer> table = new Hashtable<>();
        table.put("Ready", countReady);
        table.put("Inprogress", countInProgress);
        table.put("Completed", countComplete);
        return table;
    }
}
