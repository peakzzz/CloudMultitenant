package edu.sjsu.projectcloud.db;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.validation.constraints.Null;
import java.net.UnknownHostException;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by mallika on 5/1/15.
 */
public class SprintAccess {

    private MongoOperations getMongoOperations() throws UnknownHostException {
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "test"));
        return mongoOperations;
    }

    public Sprint insertSprint(Sprint sprint) {
        Sprint dbSprint = null;
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            mongoOperations.insert(sprint);
        }
        return sprint;
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

    public Sprint updateSprintAddTask(String sprintid, TaskScrum task) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        mongoOperations.updateFirst(query(where("_id").is(sprintid)), new Update().push("tasks", task), Sprint.class);
        Sprint sprint = mongoOperations.findOne(query(where("_id").is(sprintid)), Sprint.class);
        return sprint;
    }

    public Sprint updateSprintUpdateTask(String sprintid, Task task) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(sprintid));
        Update update = new Update().pull("tasks", new BasicDBObject("_id", task.getId()));
        mongoOperations.updateFirst(query, update, Sprint.class);
        update = new Update().push("tasks", task);
        mongoOperations.updateFirst(query, update, Sprint.class);
        Sprint sprint = mongoOperations.findOne(query, Sprint.class);
        return sprint;
    }

    public Sprint updateSprintDeleteStory(String sprintid, String taskScrumid) throws NullMongoTemplateException{
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(sprintid));
        Update update = new Update().pull("tasks", new BasicDBObject("_id", taskScrumid));
        mongoOperations.updateFirst(query, update, Sprint.class);
        Sprint sprint = mongoOperations.findOne(query(where("_id").is(sprintid)), Sprint.class);
        return sprint;
    }

    public List<TaskScrum> getStories(String sprintid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Sprint sprint = mongoOperations.findOne(query(where("_id").is(sprintid)), Sprint.class);
        List<TaskScrum> stories = sprint.getTasks();
        return stories;
    }
}
