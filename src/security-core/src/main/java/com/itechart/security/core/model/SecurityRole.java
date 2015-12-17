package com.itechart.security.core.model;

import java.util.Set;

/**
 * Security role
 *
 * @author andrei.samarou
 */
public interface SecurityRole {

    Long getId();

    String getName();

    SecurityRole getParent();

    Set<? extends SecurityPrivilege> getPrivileges();
}
