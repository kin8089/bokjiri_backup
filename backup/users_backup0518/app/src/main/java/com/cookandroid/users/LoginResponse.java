package com.cookandroid.users;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
