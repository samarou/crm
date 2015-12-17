package com.itechart.security.core.model;

/**
 * Security privilege
 *
 * @author andrei.samarou
 */
public interface SecurityPrivilege {

    String getObjectTypeName();

    String getActionName();
}