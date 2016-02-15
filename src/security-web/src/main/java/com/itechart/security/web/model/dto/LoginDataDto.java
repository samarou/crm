package com.itechart.security.web.model.dto;

import java.util.Date;

/**
 * @author andrei.samarou
 */
public class LoginDataDto {

    private String username;
    private String password;

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