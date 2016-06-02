package com.itechart.security.model.persistent;

import com.itechart.security.core.model.acl.Permission;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

/**
 * @author yauheni.putsykovich
 */
@Entity
@Table(name = "user_default_acl")
public class UserDefaultAclEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false, updatable = false)
    private Principal principal;

    @Column(name = "permission_mask")
    private int permissionMask;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public int getPermissionMask() {
        return permissionMask;
    }

    public void setPermissionMask(int permissionMask) {
        this.permissionMask = permissionMask;
    }

    public boolean hasPermission(Permission permission) {
        int mask = permission.getMask();
        return (permissionMask & mask) == mask;
    }

    public boolean addPermission(Permission permission) {
        if (!hasPermission(permission)) {
            permissionMask |= permission.getMask();
            return true;
        }
        return false;
    }

    public boolean removePermission(Permission permission) {
        if (hasPermission(permission)) {
            permissionMask |= ~permission.getMask();
            return true;
        }
        return false;
    }

    public Set<Permission> getPermissions() {
        if (permissionMask == 0) {
            return Collections.emptySet();
        }
        Set<Permission> result = EnumSet.noneOf(Permission.class);
        for (Permission permission : Permission.values()) {
            if (hasPermission(permission)) {
                result.add(permission);
            }
        }
        return result;
    }

    public void setPermissions(Set<Permission> permissions) {
        permissionMask = 0;
        if (permissions != null) {
            for (Permission permission : permissions) {
                permissionMask |= permission.getMask();
            }
        }
    }
}
