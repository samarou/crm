package com.itechart.security.core.test;

import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.test.model.SecurityPrivilegeImpl;
import com.itechart.security.core.test.model.SecurityRoleImpl;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

/**
 * @author andrei.samarou
 */
public class RoleBuilder {

    private SecurityRoleImpl role;

    private RoleBuilder(SecurityRoleImpl role) {
        this.role = role;
    }

    public static RoleBuilder create(String roleName) {
        return create(roleName, null);
    }

    public static RoleBuilder create(String roleName, SecurityRole parent) {
        Assert.hasLength(roleName, "roleName is empty");
        SecurityRoleImpl role = new SecurityRoleImpl(roleName, parent);
        return new RoleBuilder(role);
    }

    @SuppressWarnings("unchecked")
    public RoleBuilder privilege(String objectTypeName, String actionName) {
        Assert.hasLength(objectTypeName, "objectTypeName is empty");
        Assert.hasLength(actionName, "actionName is empty");
        role.addPrivilege(new SecurityPrivilegeImpl(objectTypeName, actionName));
        return this;
    }

    public SecurityRoleImpl build() {
        return (SecurityRoleImpl) SerializationUtils.deserialize(SerializationUtils.serialize(role));
    }
}
