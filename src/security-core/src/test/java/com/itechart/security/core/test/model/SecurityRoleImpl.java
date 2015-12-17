package com.itechart.security.core.test.model;

import com.itechart.security.core.model.SecurityPrivilege;
import com.itechart.security.core.model.SecurityRole;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link SecurityRole} for test purposes
 *
 * @author andrei.samarou
 */
public class SecurityRoleImpl implements SecurityRole, Serializable {

    private String name;
    private SecurityRole parent;
    private Set<SecurityPrivilege> privileges = new HashSet<>();

    public SecurityRoleImpl(String name, SecurityRole parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public Long getId() {
        return (long) name.hashCode();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SecurityRole getParent() {
        return parent;
    }

    public void setParent(SecurityRole parent) {
        this.parent = parent;
    }

    @Override
    public Set<SecurityPrivilege> getPrivileges() {
        return privileges;
    }

    public void addPrivilege(SecurityPrivilege privilege) {
        this.privileges.add(privilege);
    }
}
