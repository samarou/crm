package com.itechart.sample.model.security;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link UserDetails} for storing authentificated user information
 *
 * @author andrei.samarou
 */
public class User implements UserDetails, CredentialsContainer {

    private Long userId;
    private String username;
    private String password;
    private boolean enabled;
    private Set<GrantedAuthority> authorities;

    public User(Long userId, String username, String password, boolean enabled,
                Collection<? extends GrantedAuthority> authorities)
    {
        Assert.notNull(password, "userId is required");
        Assert.hasText(username, "username is empty");
        Assert.notNull(password, "password is required");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
    }

    public Long getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void eraseCredentials() {
        password = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return username.equals(((User) o).username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
