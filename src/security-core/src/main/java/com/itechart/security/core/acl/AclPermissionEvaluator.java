package com.itechart.security.core.acl;

import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecurityAcl;
import com.itechart.security.core.SecurityOperations;
import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Implementation of strategy used in expression evaluation to determine whether
 * a user has a permissionObject for a given domain object.
 *
 * @author andrei.samarou
 */
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final static Logger logger = LoggerFactory.getLogger(AclPermissionEvaluator.class);

    private SecurityRepository securityRepository;

    private RoleHierarchy roleHierarchy;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object permission) {
        if (object == null) {
            return false;
        }
        if (!(object instanceof ObjectIdentity)) {
            throw new IllegalArgumentException("object is not " + ObjectIdentity.class);
        }
        return checkPermission(authentication, (ObjectIdentity) object, permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable objectId, String objectType, Object permission) {
        return checkPermission(authentication, new ObjectIdentityImpl(objectId, objectType), permission);
    }

    private boolean checkPermission(Authentication authentication, ObjectIdentity oid, Object permissionObject) {
        if (logger.isDebugEnabled()) {
            logger.debug("Checking permission '" + permissionObject + "' for object '" + oid + "'");
        }
        List<Permission> requiredPermissions = resolvePermission(permissionObject);
        List<SecurityAcl> acls = securityRepository.findAcls(oid);
        if (!CollectionUtils.isEmpty(acls)) {
            Boolean permissionsFound = false;
            Long userId = SecurityUtils.getUserId(authentication);
            List<Long> principalIds = SecurityUtils.getPrincipalIds(authentication);
            for (SecurityAcl acl : acls) {
                Boolean hasRequiredPerms = hasPermissions(acl, userId, principalIds, requiredPermissions);
                if (hasRequiredPerms != null) {
                    permissionsFound = true;
                    if (!hasRequiredPerms) {
                        // permissions was found, but access isn't granted for one of acl's in hierarchy
                        if (logger.isDebugEnabled()) {
                            logger.debug("Permissions found on " + oid + ". Required permissions isn't granted in " + acl);
                        }
                        return false;
                    }
                }
            }
            if (permissionsFound) {
                // permissions was found and access is granted
                if (logger.isDebugEnabled()) {
                    logger.debug("Permissions found on " + oid + ". Required permissions is granted");
                }
                return true;
            }
        }
        // if acl or permissions on acl wasn't found then check user roles and permissions
        SecurityOperations securityOperations = new SecurityOperations(authentication);
        securityOperations.setRoleHierarchy(roleHierarchy);
        boolean granted = true;
        for (Permission requiredPermission : requiredPermissions) {
            // todo нужен маппинг привилегий на роли
            granted &= securityOperations.hasPrivilege(oid.getObjectType(), requiredPermission.name());
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Have no any permissions on " + oid + ", but required permissions granted througth roles/privileges");
        }
        return granted;
    }


    /**
     * Method checks that principals have required permissions on acl.
     * Returns: true - if permissions was granted (or current principal is object owner);
     * false - if any permissions was found, but required permissions wasn't granted,
     * null - if permissions wasn't found for principals on acl
     */
    private Boolean hasPermissions(SecurityAcl acl, Long userId, List<Long> principalIds, List<Permission> requiredPermissions) {
        if (userId.equals(acl.getOwnerId())) {
            return true;
        }
        boolean permissionsFound = false;
        for (Permission requiredPermission : requiredPermissions) {
            boolean permitted = false;
            for (Long principalId : principalIds) {
                Set<Permission> grantedPermissions = acl.getPermissions(principalId);
                if (grantedPermissions != null) {
                    permissionsFound = true;
                    if (hasPermission(grantedPermissions, requiredPermission)) {
                        permitted = true;
                        break;
                    }
                }
            }
            if (permissionsFound && !permitted) {
                return false;
            }
        }
        return permissionsFound ? true : null;
    }

    private boolean hasPermission(Set<Permission> grantedPermissions, Permission requiredPermission) {
        for (Permission grantedPermission : grantedPermissions) {
            if (grantedPermission.isAllow(requiredPermission)) {
                return true;
            }
        }
        return false;
    }

    private List<Permission> resolvePermission(Object permissionObject) {
        if (permissionObject instanceof String) {
            Permission permission = Permission.valueOf((String) permissionObject);
            return Collections.singletonList(permission);
        }
        if (permissionObject instanceof Permission) {
            return Collections.singletonList((Permission) permissionObject);
        }
        if (permissionObject instanceof Permission[]) {
            return Arrays.asList((Permission[]) permissionObject);
        }
        throw new IllegalArgumentException("Unsupported permissionObject: " + permissionObject);
    }

    @Required
    public void setSecurityRepository(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }
}
