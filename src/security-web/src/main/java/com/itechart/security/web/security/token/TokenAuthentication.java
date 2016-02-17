package com.itechart.security.web.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Represents a token-based {@link Authentication}.
 * Provides security token that contains user-specific information
 * and can be used for user authentication
 *
 * @author andrei.samarou
 */
public final class TokenAuthentication implements Authentication, CredentialsContainer {

    private Object principal;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean authenticated = false;

    public TokenAuthentication(String token) {
        Assert.notNull(token, "token is required");
        this.principal = token;
        this.authorities = AuthorityUtils.NO_AUTHORITIES;
    }

    public TokenAuthentication(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(userDetails, "userDetails is required");
        this.principal = userDetails;
        this.authorities = authorities;
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use appropriated constructor");
    }

    @Override
    public void eraseCredentials() {
        if (principal instanceof CredentialsContainer) {
            ((CredentialsContainer) principal).eraseCredentials();
        } else {
            principal = null;
        }
    }

    @Override
    public String getName() {
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return "";
    }
}