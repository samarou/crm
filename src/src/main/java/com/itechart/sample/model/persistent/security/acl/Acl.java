package com.itechart.sample.model.persistent.security.acl;

import com.itechart.sample.model.persistent.BaseEntity;
import com.itechart.sample.model.persistent.security.ObjectType;
import com.itechart.sample.model.persistent.security.Principal;
import com.itechart.sample.model.security.Permission;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ACL entity
 *
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "object_type_id", nullable = false, updatable = false)
    private ObjectType objectType;

    @Column(name = "object_id", nullable = false, updatable = false)
    private Long objectId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", updatable = false)
    private Acl parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", updatable = false)
    private Principal owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "object")
    private Set<AclEntry> entries;

    @Column(name = "inheriting")
    private boolean inheriting;

    public Acl(ObjectType objectType, Long objectId) {
        this(objectType, objectId, null, null);
    }

    public Acl(ObjectType objectType, Long objectId, Acl parent, Principal owner) {
        Assert.notNull(objectType);
        Assert.notNull(objectId);
        this.objectType = objectType;
        this.objectId = objectId;
        this.parent = parent;
        this.owner = owner;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Acl getParent() {
        return parent;
    }

    public void setParent(Acl parent) {
        this.parent = parent;
    }

    public Principal getOwner() {
        return owner;
    }

    public void setOwner(Principal owner) {
        this.owner = owner;
    }

    public Set<AclEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<AclEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(AclEntry entry) {
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

    private AclEntry findEntry(Long principalId) {
        for (AclEntry entry : getEntries()) {
            if (entry.getPrincipalId().equals(principalId)) {
                return entry;
            }
        }
        return null;
    }
}