package com.itechart.security.core.acl;

import com.itechart.security.core.SecurityOperations;
import com.itechart.security.core.SecurityRepository;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecurityAcl;
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
        List<Permission> requiredPermissions = resolvePermission(permissionObject);
        if (isGrantedWithAcl(authentication, oid, requiredPermissions)) {
            if (isGrantedWithPrivilege(authentication, oid, requiredPermissions)) {
                logger.debug("{} on {} is granted", permissionObject, oid);
                return true;
            }
        }
        logger.debug("{} on {} is not granted", permissionObject, oid);
        return false;
    }

    private boolean isGrantedWithPrivilege(Authentication authentication, ObjectIdentity oid, List<Permission> requiredPermissions) {
        boolean granted = true;
        SecurityOperations securityOperations = new SecurityOperations(authentication);
        securityOperations.setRoleHierarchy(roleHierarchy);
        for (Permission requiredPermission : requiredPermissions) {
            // todo нужен маппинг привилегий на роли
            granted &= securityOperations.hasPrivilege(oid.getObjectType(), requiredPermission.name());
        }
        return granted;
    }

    private boolean isGrantedWithAcl(Authentication authentication, ObjectIdentity oid, List<Permission> requiredPermissions) {
        boolean permissionsFound = false;
        List<SecurityAcl> acls = securityRepository.findAcls(oid);
        if (!CollectionUtils.isEmpty(acls)) {
            Long userId = SecurityUtils.getUserId(authentication);
            List<Long> principalIds = SecurityUtils.getPrincipalIds(authentication);
            for (SecurityAcl acl : acls) {
                Boolean hasRequiredPerms = hasPermissions(acl, userId, principalIds, requiredPermissions);
                if (hasRequiredPerms != null) {
                    permissionsFound = true;
                    if (!hasRequiredPerms) {
                        // permissions was found, but access isn't granted for one of acl's in hierarchy
                        logger.debug("Permissions found on {}. Required permissions isn't granted in {}", oid, acl);
                        return false;
                    }
                }
            }
        }
        return permissionsFound;
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
