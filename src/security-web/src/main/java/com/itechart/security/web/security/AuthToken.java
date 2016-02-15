package com.itechart.security.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.token.DefaultToken;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;

/**
 * @author yauheni.putsykovich
 */
public class AuthToken extends DefaultToken {
    private String ip;
    private String id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
