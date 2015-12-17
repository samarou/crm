package com.itechart.sample.model.persistent.security.acl;

import com.itechart.sample.model.persistent.BaseEntity;
import com.itechart.security.core.model.acl.Permission;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * ACL entry.
 * Describes permissions that <code>principal</code> has on object
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "acl_entry")
public class AclEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_identity_id", nullable = false, updatable = false)
    private Acl acl;

    @Column(name = "principal_id", nullable = false, updatable = false)
    private Long principalId;

    @Column(name = "permission_mask")
    private int permissionMask;

    public AclEntry() {
    }

    public AclEntry(Acl acl, Long principalId, Set<Permission> permissions) {
        Assert.notNull(acl);
        Assert.notNull(principalId);
        this.acl = acl;
        this.principalId = principalId;
        setPermissions(permissions);
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acl getAcl() {
        return acl;
    }

    public void setAcl(Acl acl) {
        this.acl = acl;
    }

    public Long getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
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
