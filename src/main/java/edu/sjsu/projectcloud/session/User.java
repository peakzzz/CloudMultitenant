package edu.sjsu.projectcloud.session;

import edu.sjsu.projectcloud.resource.Resource;

/**
 * Created by dj on 5/2/15.
 */
public class User {
    private String id = "";
    private String username = "";
    private String password = "";

    public User() {}

    public User(Resource r) {
        this.id = r.getId();
        this.username = r.getUsername();
        this.password = r.getPassword();
    }

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
}
