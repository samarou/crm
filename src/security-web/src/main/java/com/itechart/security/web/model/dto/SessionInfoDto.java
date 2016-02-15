package com.itechart.security.web.model.dto;

import org.springframework.security.core.token.Token;

/**
 * For storing information about the logged user's session.
 * It isn't related to HTTP session and only stores logged user's
 * information and authentication token
 *
 * @author andrei.samarou
 */
public class SessionInfoDto {

    private String username;
    private String token;

    public SessionInfoDto(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
