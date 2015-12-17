package com.itechart.security.hibernate.filter;

import com.itechart.security.core.SecurityOperations;
import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecuredObject;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * Factory produces parameters values for security filters
 *
 * @author andrei.samarou
 */
public class FilterParamsBuilder {

    private SecurityRepository securityRepository;
    private RoleHierarchy roleHierarchy;

    /**
     * Build filter parameters source
     *
     * @param objectType  type of filtered object
     * @param permissions list of permissions
     */
    public FilterParamsSource build(Class<?> objectType, Set<Permission> permissions) {
        Assert.isAssignable(SecuredObject.class, objectType,
                "objectType is not subclass of " + SecuredObject.class);
        Assert.notEmpty(permissions, "permissions is empty");

        FilterParamsSource params = new FilterParamsSource();

        String objectTypeName;
        try {
            Constructor constructor = objectType.getConstructor();
            SecuredObject instance = (SecuredObject) constructor.newInstance();
            objectTypeName = instance.getObjectType();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No-argument constructor is required in " + objectType);
        }

        Long objectTypeId = securityRepository.getObjectTypeIdByName(objectTypeName);
        if (objectTypeId == null) {
            throw new RuntimeException("Can't find object type by name: " + objectTypeName);
        }

        params.setObjectTypeId(objectTypeId);

        params.setPrincipleIds(SecurityUtils.getAuthenticatedPrincipalIds());
        params.setUserId(SecurityUtils.getAuthenticatedUserId());

        SecurityOperations securityOperations = SecurityUtils.getSecurityOperations();
        securityOperations.setRoleHierarchy(roleHierarchy);

        int permissionMask = 0;
        boolean hasPrivilege = true;

        for (Permission permission : permissions) {
            permissionMask |= permission.getMask();
            // todo нужен маппинг привилегий на роли
            hasPrivilege &= securityOperations.hasPrivilege(objectTypeName, permission.name());
        }

        params.setPermissionMask(permissionMask);
        params.setHasPrivilege(hasPrivilege);

        return params;
    }

    @Required
    public void setSecurityRepository(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }
}
