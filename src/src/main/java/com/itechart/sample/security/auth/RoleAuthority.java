package com.itechart.sample.security.auth;

import com.itechart.sample.model.persistent.security.Privilege;
import com.itechart.sample.model.persistent.security.Role;
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

    public RoleAuthority(Role role) {
        Assert.notNull(role);
        Assert.hasText(role.getName());
        this.role = role.getName();
        this.privileges = Collections.emptySet();
        Set<Privilege> rolePrivileges = role.getPrivileges();
        if (rolePrivileges != null && rolePrivileges.size() > 0) {
            HashSet<PrivilegeAuthority> privilegeAuthorities = new HashSet<>(rolePrivileges.size());
            for (Privilege privilege : rolePrivileges) {
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