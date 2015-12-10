package com.itechart.sample.security.util;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;
import com.itechart.sample.model.security.Permission;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author andrei.samarou
 */
public class AclBuilder {

    private AclObjectKey key;
    private String ownerName;
    private Acl parent;

    private Map<String, Set<Permission>> permissions = new HashMap<>();

    private AclBuilder(Long objectTypeId, Long objectId) {
        this.key = new AclObjectKey(objectTypeId, objectId);
    }

    public static AclBuilder create(Long objectTypeId, Long objectId) {
        return new AclBuilder(objectTypeId, objectId);
    }

    public AclBuilder owner(String ownerName) {
        Assert.hasLength(ownerName, "ownerName is empty");
        this.ownerName = ownerName;
        return this;
    }

    public AclBuilder parent(Acl parent) {
        Assert.notNull(parent, "parent is null");
        this.parent = parent;
        return this;
    }

    public AclBuilder privilege(String userName, Permission... permission) {
        Assert.hasLength(userName, "userName is empty");
        Assert.notEmpty(permission, "permission is empty");
        Set<Permission> userPermissions = permissions.get(userName);
        if (userPermissions == null) {
            userPermissions = EnumSet.noneOf(Permission.class);
            permissions.put(userName, userPermissions);
        }
        userPermissions.addAll(Arrays.asList(permission));
        return this;
    }

    public Acl build() {
        Acl acl = new Acl(key,
                parent != null ? (long) parent.getObjectKey().hashCode() : null,
                ownerName != null ? (long) ownerName.hashCode() : null);
        for (Map.Entry<String, Set<Permission>> entry : permissions.entrySet()) {
            acl.addPermissions((long) entry.getKey().hashCode(), entry.getValue());
        }
        return acl;
    }
}
