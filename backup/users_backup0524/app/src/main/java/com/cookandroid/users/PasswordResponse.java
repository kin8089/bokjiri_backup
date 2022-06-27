package com.cookandroid.users;

import java.io.Serializable;

public class PasswordResponse implements Serializable {

    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
