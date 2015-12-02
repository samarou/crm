package com.itechart.sample.security.util;

import com.itechart.sample.model.persistent.security.*;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author andrei.samarou
 */
public class UserBuilder {

    private User user;
    private Role lastRole;

    private UserBuilder(User user) {
        this.user = user;
    }

    public static UserBuilder create(String userName) {
        Assert.hasLength(userName, "userName is empty");
        User user = new User();
        user.setId((long) userName.hashCode());
        user.setUserName(userName);
        user.setPassword(userName);
        user.setActive(true);
        return new UserBuilder(user);
    }

    public UserBuilder group(String... groupNames) {
        Set<Group> userGroups = user.getGroups();
        if (userGroups == null) {
            userGroups = new HashSet<>();
            user.setGroups(userGroups);
        }
        for (String groupName : groupNames) {
            Assert.hasLength(groupName, "groupName is empty");
            if (user.getName().equals(groupName)) {
                throw new RuntimeException("User and group name have to be different");
            }
            Group group = new Group();
            group.setId((long) groupName.hashCode());
            group.setName(groupName);
            userGroups.add(group);
        }
        return this;
    }

    public UserBuilder role(String roleName) {
        Assert.hasLength(roleName, "roleName is empty");
        Role role = new Role();
        role.setId((long) roleName.hashCode());
        role.setName(roleName);
        return role(role);
    }

    public UserBuilder role(Role... roles) {
        Assert.notNull(roles, "roles is null");
        for (Role role : roles) {
            Assert.notNull(role, "role is null");
            Set<Role> userRoles = user.getRoles();
            if (userRoles == null) {
                userRoles = new HashSet<>();
                user.setRoles(userRoles);
            }
            userRoles.add(role);
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
        Set<Privilege> rolePrivileges = lastRole.getPrivileges();
        if (rolePrivileges == null) {
            rolePrivileges = new HashSet<>();
            lastRole.setPrivileges(rolePrivileges);
        }
        Privilege privilege = new Privilege();

        ObjectType objectType = new ObjectType();
        objectType.setId((long) objectTypeName.hashCode());
        objectType.setName(objectTypeName);
        privilege.setObjectType(objectType);

        Action action = new Action();
        action.setId((long) actionName.hashCode());
        action.setName(actionName);
        privilege.setAction(action);

        rolePrivileges.add(privilege);

        return this;
    }

    public User build() {
        return (User) SerializationUtils.deserialize(SerializationUtils.serialize(user));
    }

}
