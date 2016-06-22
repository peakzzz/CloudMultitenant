package edu.sjsu.projectcloud.resource;

import edu.sjsu.projectcloud.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mallika on 4/30/15.
 */
public class Resource {
    protected String id;
    protected String username;
    protected String password;
    List<Project> projects = new ArrayList<>();

    public Resource(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Resource() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    @Override
    public String toString() {
        String string = "id: "+this.getId()+", username: "+this.getUsername()+", password: "+this.getPassword();
        return string;
    }
}
