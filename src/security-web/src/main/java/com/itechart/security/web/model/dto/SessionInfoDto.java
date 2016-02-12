package com.itechart.security.web.model.dto;

/**
 * For storing information about the logged user's session.
 * It isn't related to HTTP session and only stores logged user's
 * information and authentication token
 *
 * @author andrei.samarou
 */
public class SessionInfoDto {

    private String username;
    private String tocken;

    public SessionInfoDto(String username, String tocken) {
        this.username = username;
        this.tocken = tocken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTocken() {
        return tocken;
    }

    public void setTocken(String tocken) {
        this.tocken = tocken;
    }
}
