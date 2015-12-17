package com.itechart.security.core.test.model;

import com.itechart.security.core.model.SecurityGroup;
import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link SecurityUser} for test purposes
 *
 * @author andrei.samarou
 */
public class SecurityUserImpl implements SecurityUser, Serializable {

    private String userName;
    private boolean active;

    private Set<SecurityRole> roles = new HashSet<>();
    private Set<SecurityGroup> groups = new HashSet<>();

    public SecurityUserImpl(String userName, boolean active) {
        this.userName = userName;
        this.active = active;
    }

    @Override
    public Long getId() {
        return (long) userName.hashCode();
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return userName;
    }

    @Override
    public Set<SecurityRole> getRoles() {
        return roles;
    }

    public void addRole(SecurityRole role) {
        roles.add(role);
    }

    @Override
    public Set<SecurityGroup> getGroups() {
        return groups;
    }

    public void addGroup(SecurityGroup group) {
        if (userName.equals(group.getName())) {
            throw new RuntimeException("SecurityUser and group names have to be different. Id is based on names.");
        }
        groups.add(group);
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
