package com.cookandroid.users;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private int id;
    private String username;
    private String access;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
