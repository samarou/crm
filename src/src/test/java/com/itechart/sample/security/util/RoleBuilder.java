package com.itechart.sample.security.util;

import com.itechart.sample.model.persistent.security.Action;
import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.Privilege;
import com.itechart.sample.model.persistent.security.Role;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * @author andrei.samarou
 */
public class RoleBuilder {

    private Role role;

    private RoleBuilder(Role role) {
        this.role = role;
    }

    public static RoleBuilder create(String roleName) {
        return create(roleName, null);
    }

    public static RoleBuilder create(String roleName, Role parent) {
        Assert.hasLength(roleName, "roleName is empty");
        Role role = new Role();
        role.setId((long) roleName.hashCode());
        role.setName(roleName);
        role.setParent(parent);
        return new RoleBuilder(role);
    }

    public RoleBuilder privilege(String objectTypeName, String actionName) {
        Assert.hasLength(objectTypeName, "objectTypeName is empty");
        Assert.hasLength(actionName, "actionName is empty");
        Set<Privilege> rolePrivileges = role.getPrivileges();
        if (rolePrivileges == null) {
            rolePrivileges = new HashSet<>();
            role.setPrivileges(rolePrivileges);
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


    public Role build() {
        return (Role) SerializationUtils.deserialize(SerializationUtils.serialize(role));
    }
}
