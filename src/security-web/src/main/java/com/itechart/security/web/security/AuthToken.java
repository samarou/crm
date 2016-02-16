package com.itechart.security.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.token.DefaultToken;

import java.util.Collection;

/**
 * Container to information about user to build authentication token.
 *
 * @author yauheni.putsykovich
 * @see DefaultToken
 */
public class AuthToken extends DefaultToken {
    private String ip;
    private String id;
    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities;

    public AuthToken(String key, long keyCreationTime, String extendedInformation) {
        super(key, keyCreationTime, extendedInformation);
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
