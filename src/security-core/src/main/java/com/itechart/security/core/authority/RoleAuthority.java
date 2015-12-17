package com.itechart.security.core.authority;

import com.itechart.security.core.model.SecurityPrivilege;
import com.itechart.security.core.model.SecurityRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * RoleAuthority represents role granted to user
 *
 * @author andrei.samarou
 */
public class RoleAuthority implements GrantedAuthority {

    private String role;
    private Set<PrivilegeAuthority> privileges;

    public RoleAuthority(SecurityRole role) {
        Assert.notNull(role);
        Assert.hasText(role.getName());
        this.role = role.getName();
        this.privileges = Collections.emptySet();
        Set<? extends SecurityPrivilege> rolePrivileges = role.getPrivileges();
        if (rolePrivileges != null && rolePrivileges.size() > 0) {
            HashSet<PrivilegeAuthority> privilegeAuthorities = new HashSet<>(rolePrivileges.size());
            for (SecurityPrivilege privilege : rolePrivileges) {
                privilegeAuthorities.add(new PrivilegeAuthority(privilege));
            }
            this.privileges = Collections.unmodifiableSet(privilegeAuthorities);
        }
    }

    public String getRole() {
        return role;
    }

    public Set<PrivilegeAuthority> getPrivileges() {
        return privileges;
    }

    @Override
    public String getAuthority() {
        return "ROLE:" + role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return role.equals(((RoleAuthority) o).role);
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }
}