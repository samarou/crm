package com.itechart.sample.security.acl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.security.ObjectIdentity;
import com.itechart.sample.model.security.ObjectIdentityImpl;
import com.itechart.sample.model.security.Permission;
import com.itechart.sample.model.security.User;
import com.itechart.sample.security.SecurityOperations;
import com.itechart.sample.security.auth.GroupAuthority;
import com.itechart.sample.service.AclService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Implementation of strategy used in expression evaluation to determine whether
 * a user has a permissionObject for a given domain object.
 *
 * @author andrei.samarou
 */
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final static Log logger = LogFactory.getLog(AclPermissionEvaluator.class);

    private AclService aclService;

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
        List<Acl> acls = aclService.findAclWithAncestors(oid);
        if (!CollectionUtils.isEmpty(acls)) {
            Boolean permissionsFound = false;
            List<Long> principalIds = getAuthenticationPrincipalIds(authentication);
            for (Acl acl : acls) {
                Boolean hasRequiredPerms = hasPermissions(acl, principalIds, requiredPermissions);
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
    private Boolean hasPermissions(Acl acl, List<Long> principalIds, List<Permission> requiredPermissions) {
        if (acl.getOwnerId() != null) {
            if (principalIds.contains(acl.getOwnerId())) {
                return true;
            }
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

    private List<Long> getAuthenticationPrincipalIds(Authentication authentication) {
        List<Long> result = new ArrayList<>();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            result.add(((User) principal).getUserId());
        }
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority instanceof GroupAuthority) {
                result.add(((GroupAuthority) authority).getGroupId());
            }
        }
        return result;
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
    public void setAclService(AclService aclService) {
        this.aclService = aclService;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }
}
