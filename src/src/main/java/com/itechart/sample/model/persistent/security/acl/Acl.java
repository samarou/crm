package com.itechart.sample.model.persistent.security.acl;

import com.itechart.sample.model.persistent.BaseEntity;
import com.itechart.sample.model.security.Permission;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ACL entity.
 * Represents a single domain object instance with some additional information
 * like ownership and inheritance. Contains permissions for principals on this object
 *
 * @author andrei.samarou
 */
@Entity
@Table(name = "acl_object_identity")
public class Acl extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private AclObjectKey objectKey;

    @Column(name = "parent_id", updatable = false)
    private Long parentId;

    @Column(name = "owner_id", updatable = false)
    private Long ownerId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "acl")
    private Set<AclEntry> entries;

    @Column(name = "inheriting")
    private boolean inheriting;

    public Acl() {
    }

    public Acl(AclObjectKey objectKey) {
        this(objectKey, null, null);
    }

    public Acl(AclObjectKey objectKey, Long parentId, Long ownerId) {
        Assert.notNull(objectKey);
        this.objectKey = objectKey;
        this.parentId = parentId;
        this.ownerId = ownerId;
        this.inheriting = parentId != null;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclObjectKey getObjectKey() {
        return objectKey;
    }

    public Long getParentId() {
        return parentId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    protected Set<AclEntry> getEntries() {
        return entries;
    }

    protected void addEntry(AclEntry entry) {
        if (entries == null) {
            entries = new HashSet<>();
        }
        entries.add(entry);
    }

    public boolean isInheriting() {
        return inheriting;
    }

    public void setInheriting(boolean inheriting) {
        this.inheriting = inheriting;
    }

    public boolean addPermission(Long principalId, Permission permission) {
        return addPermissions(principalId, Collections.singleton(permission));
    }

    public boolean addPermissions(Long principalId, Set<Permission> permissions) {
        AclEntry entry = findEntry(principalId);
        if (entry == null) {
            addEntry(new AclEntry(this, principalId, permissions));
            return true;
        } else {
            boolean result = true;
            for (Permission permission : permissions) {
                result &= entry.addPermission(permission);
            }
            return result;
        }
    }

    public void setPermissions(Long principalId, Set<Permission> permissions) {
        AclEntry entry = findEntry(principalId);
        if (entry == null) {
            addEntry(new AclEntry(this, principalId, permissions));
        } else {
            entry.setPermissions(permissions);
        }
    }

    public boolean removePermission(Long principalId, Permission permission) {
        AclEntry entry = findEntry(principalId);
        return entry != null && entry.removePermission(permission);
    }

    public boolean hasPermission(Long principalId, Permission permission) {
        AclEntry entry = findEntry(principalId);
        return entry != null && entry.hasPermission(permission);
    }

    public Set<Permission> getPermissions(Long principalId) {
        AclEntry entry = findEntry(principalId);
        return entry != null ? entry.getPermissions() : null;
    }

    public void denyAll(Long principalId) {
        setPermissions(principalId, null);
    }

    public boolean hasAnyPermissions() {
        return entries != null && !entries.isEmpty();
    }

    private AclEntry findEntry(Long principalId) {
        if (entries != null) {
            for (AclEntry entry : entries) {
                if (entry.getPrincipalId().equals(principalId)) {
                    return entry;
                }
            }
        }
        return null;
    }
}