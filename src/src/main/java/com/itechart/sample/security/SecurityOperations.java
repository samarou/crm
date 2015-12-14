package com.itechart.sample.security;

import com.itechart.sample.security.auth.PrivilegeAuthority;
import com.itechart.sample.security.auth.RoleAuthority;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Set of utility methods for performing security checking operations
 * on the {@link Authentication}
 *
 * @author andrei.samarou
 */
public class SecurityOperations {

    private RoleHierarchy roleHierarchy;

    // Current authenticated principal
    private Authentication authentication;

    // Granted and inherited roles of current authentication
    private Set<RoleAuthority> grantedRoles;

    public SecurityOperations(Authentication authentication) {
        Assert.notNull(authentication, "Authentication object cannot be null");
        this.authentication = authentication;
        if (!authentication.isAuthenticated()) {
            throw new AuthenticationException("Principal is not fully authenticated");
        }
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public final boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    public final boolean hasAnyRole(String... roles) {
        if (grantedRoles != null) {
            // if all granted roles was loaded then search in they
            return containsAnyRole(grantedRoles, roles);
        } else {
            // fistly, try to find target roles in first-level user roles
            if (containsAnyRole(authentication.getAuthorities(), roles)) {
                return true;
            }
            // then search in all inherited roles
            grantedRoles = getGrantedInheritedRoles();
            return containsAnyRole(grantedRoles, roles);
        }
    }

    public final boolean hasPrivilege(String objectType, String action) {
        return hasAnyPrivilege(objectType, action);
    }

    public final boolean hasAnyPrivilege(String objectType, String... action) {
        // fistly, try to find target privileges in user-granted privileges
        if (containsAnyPrivilege(authentication.getAuthorities(), objectType, action)) {
            return true;
        }
        // then, search target privileges in role-granted privileges
        if (grantedRoles != null) {
            return containsAnyRolePrivilege(grantedRoles, objectType, action);
        } else {
            if (containsAnyRolePrivilege(authentication.getAuthorities(), objectType, action)) {
                return true;
            }
            grantedRoles = getGrantedInheritedRoles();
            return containsAnyRolePrivilege(grantedRoles, objectType, action);
        }
    }

    private Set<RoleAuthority> getGrantedInheritedRoles() {
        if (grantedRoles != null) {
            return grantedRoles;
        }
        grantedRoles = new HashSet<>();
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        for (GrantedAuthority userAuthority : userAuthorities) {
            if (userAuthority instanceof RoleAuthority) {
                grantedRoles.add((RoleAuthority) userAuthority);
            }
        }
        if (roleHierarchy != null && !grantedRoles.isEmpty()) {
            Collection<? extends GrantedAuthority> inheritedAuthorities =
                    roleHierarchy.getReachableGrantedAuthorities(Collections.unmodifiableSet(grantedRoles));
            for (GrantedAuthority inheritedAuthority : inheritedAuthorities) {
                if (inheritedAuthority instanceof RoleAuthority) {
                    grantedRoles.add((RoleAuthority) inheritedAuthority);
                }
            }
        }
        return grantedRoles;
    }

    private boolean containsAnyRole(Collection<? extends GrantedAuthority> authorities, String... roles) {
        if (CollectionUtils.isEmpty(authorities) || roles == null) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof RoleAuthority) {
                String userRole = ((RoleAuthority) authority).getRole();
                for (String role : roles) {
                    if (userRole.equals(role)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsAnyPrivilege(Collection<? extends GrantedAuthority> authorities, String objectType, String... actions) {
        if (CollectionUtils.isEmpty(authorities) || objectType == null || actions == null) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof PrivilegeAuthority) {
                PrivilegeAuthority privilege = ((PrivilegeAuthority) authority);
                if (privilege.getObjectType().equals(objectType)) {
                    for (String action : actions) {
                        if (privilege.getAction().equals(action)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean containsAnyRolePrivilege(Collection<? extends GrantedAuthority> authorities, String objectType, String... actions) {
        if (CollectionUtils.isEmpty(authorities)) {
            return false;
        }
        for (GrantedAuthority authority : authorities) {
            if (authority instanceof RoleAuthority) {
                if (containsAnyPrivilege(((RoleAuthority) authority).getPrivileges(), objectType, actions)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }
}
