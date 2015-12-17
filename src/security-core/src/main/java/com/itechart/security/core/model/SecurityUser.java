package com.itechart.security.core.model;

import java.util.Set;

/**
 * Security user
 *
 * @author andrei.samarou
 */
public interface SecurityUser {

    Long getId();

    String getUserName();

    String getPassword();

    Set<? extends SecurityRole> getRoles();

    Set<? extends SecurityGroup> getGroups();

    boolean isActive();
}
