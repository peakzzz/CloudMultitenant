package edu.sjsu.projectcloud.db;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import edu.sjsu.projectcloud.project.Project;
import edu.sjsu.projectcloud.resource.Resource;
import edu.sjsu.projectcloud.sprint.Sprint;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.net.UnknownHostException;
import java.util.List;


/**
 * Created by mallika on 5/1/15.
 */
public class ResourceAccess {

    private MongoOperations getMongoOperations() throws UnknownHostException {
        MongoOperations mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new Mongo(), "test"));
        return mongoOperations;
    }

    public boolean insertResource(Resource resource) {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            mongoOperations.insert(resource);
            return true;
        }
        return false;
    }

    public Resource findResource(String userName) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Resource resource = mongoOperations.findOne(query(where("username").is(userName)), Resource.class);
        return resource;
    }


    public List<Resource> getAllResources() {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations != null) {
            List<Resource> resourceList = mongoOperations.findAll(Resource.class);
            return resourceList;
        }
        return null;
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

    public Project findProjectinResource(String username, String projectName, String projectType) throws  NullMongoTemplateException{
        Project project = null;
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Resource resource = mongoOperations.findOne(query(where("username").is(username)), Resource.class);
        if(resource != null){
            List<Project> projects = resource.getProjects();
            for (Project project1 : projects) {
                if (project1.getProjecttype().equals(projectType) && project1.getProjectname().equals(projectName)) {
                    project = project1;
                    break;
                }
            }
        }
        return project;
    }

    public void updateResourceAddProject(String username, Project project) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        mongoOperations.updateFirst(query(where("username").is(username)), new Update().push("projects", project), Resource.class);
    }

    public List<Project> getProjects(String userid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Resource resource = mongoOperations.findOne(query(where("_id").is(userid)), Resource.class);
        List<Project> projects = resource.getProjects();
        return projects;
    }

    public void addProjectToResource(Project project, String userid) throws NullMongoTemplateException {
        MongoOperations mongoOperations = getMongoOperationInstance();
        if (mongoOperations == null) {
            throw new NullMongoTemplateException();
        }
        Query query = new Query(where("_id").is(userid));
        Update update = new Update().push("projects", project);
        mongoOperations.updateFirst(query, update, Resource.class);
    }
}
