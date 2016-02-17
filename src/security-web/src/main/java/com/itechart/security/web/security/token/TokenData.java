package com.itechart.security.web.security.token;

/**
 * Contains source data for building authentication token
 *
 * @author andrei.samarou
 */
public class TokenData {
    /**
     * User name
     */
    private String username;
    /**
     * User IP address
     */
    private String remoteAddr;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }
}