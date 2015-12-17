package com.itechart.security.core.test.model;

import com.itechart.security.core.model.SecurityPrivilege;

import java.io.Serializable;

/**
 * Implementation of {@link SecurityPrivilege} for test purposes
 *
 * @author andrei.samarou
 */
public class SecurityPrivilegeImpl implements SecurityPrivilege, Serializable {

    private String objectTypeName;
    private String actionName;

    public SecurityPrivilegeImpl(String objectTypeName, String actionName) {
        this.objectTypeName = objectTypeName;
        this.actionName = actionName;
    }

    @Override
    public String getObjectTypeName() {
        return objectTypeName;
    }

    @Override
    public String getActionName() {
        return actionName;
    }
}
