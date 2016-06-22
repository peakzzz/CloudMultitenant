package edu.sjsu.projectcloud.db;

import com.mongodb.Mongo;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.sprint.Sprint;
import edu.sjsu.projectcloud.task.Task;
import edu.sjsu.projectcloud.task.TaskScrum;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Update;

import java.net.UnknownHostException;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by mallika on 5/1/15.
 */
public class TaskAccess {

    private MongoOperations getMongoOperations() throws UnknownHostException {
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "test"));
        return mongoOperations;
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

    public Task insertTask(Task task) {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {

            mongoOperations.insert(task, "task");
            System.out.println(task.getId());
        }
        return task;
    }

    public void deleteTask(String taskid) {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            mongoOperations.findAndRemove(query(where("_id").is(taskid)), Task.class);
        }
    }

    public Task updateTask(Task task) {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            mongoOperations.findAndRemove(query(where("_id").is(task.getId())), Task.class);
            mongoOperations.insert(task, "task");
        }
        return task;
    }
}
