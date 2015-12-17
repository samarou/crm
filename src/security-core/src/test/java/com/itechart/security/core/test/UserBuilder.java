package com.itechart.security.core.test;

import com.itechart.security.core.test.model.SecurityGroupImpl;
import com.itechart.security.core.test.model.SecurityPrivilegeImpl;
import com.itechart.security.core.test.model.SecurityRoleImpl;
import com.itechart.security.core.test.model.SecurityUserImpl;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

/**
 * @author andrei.samarou
 */
public class UserBuilder {

    private SecurityUserImpl user;
    private SecurityRoleImpl lastRole;

    private UserBuilder(SecurityUserImpl user) {
        this.user = user;
    }

    public static UserBuilder create(String userName) {
        return create(userName, true);
    }

    public static UserBuilder create(String userName, boolean active) {
        Assert.hasLength(userName, "userName is empty");
        SecurityUserImpl user = new SecurityUserImpl(userName, active);
        return new UserBuilder(user);
    }

    public UserBuilder group(String... groupNames) {
        for (String groupName : groupNames) {
            Assert.hasLength(groupName, "groupName is empty");
            user.addGroup(new SecurityGroupImpl(groupName));
        }
        return this;
    }

    public UserBuilder role(String roleName) {
        Assert.hasLength(roleName, "roleName is empty");
        return role(new SecurityRoleImpl(roleName, null));
    }

    public UserBuilder role(SecurityRoleImpl... roles) {
        Assert.notNull(roles, "roles is null");
        for (SecurityRoleImpl role : roles) {
            Assert.notNull(role, "role is null");
            user.addRole(role);
            lastRole = role;
        }
        return this;
    }

    public UserBuilder privilege(String objectTypeName, String actionName) {
        Assert.hasLength(objectTypeName, "objectTypeName is empty");
        Assert.hasLength(actionName, "actionName is empty");
        if (lastRole == null) {
            throw new RuntimeException("Add least one role to user");
        }
        lastRole.addPrivilege(new SecurityPrivilegeImpl(objectTypeName, actionName));
        return this;
    }

    public SecurityUserImpl build() {
        return (SecurityUserImpl) SerializationUtils.deserialize(SerializationUtils.serialize(user));
    }
}
