package com.itechart.security.core.test;

import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecurityAcl;
import com.itechart.security.core.test.model.SecurityAclImpl;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * @author andrei.samarou
 */
public class AclBuilder {

    private AclObjectKey key;
    private String ownerName;

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

    public AclBuilder privilege(String userName, Permission... permission) {
        Assert.hasLength(userName, "userName is empty");
        Assert.notEmpty(permission, "permission is empty");
        Set<Permission> userPermissions = permissions.computeIfAbsent(
                userName, s -> EnumSet.noneOf(Permission.class));
        userPermissions.addAll(Arrays.asList(permission));
        return this;
    }

    public SecurityAclImpl build() {
        SecurityAclImpl acl = new SecurityAclImpl(key, ownerName != null ? (long) ownerName.hashCode() : null);
        permissions.forEach((userName, permissions) -> acl.addPermissions((long) userName.hashCode(), permissions));
        return acl;
    }

    private class AclObjectKey implements Serializable {

        private Long objectTypeId;
        private Long objectId;

        public AclObjectKey(Long objectTypeId, Long objectId) {
            this.objectTypeId = objectTypeId;
            this.objectId = objectId;
        }

        public Long getObjectTypeId() {
            return objectTypeId;
        }

        public Long getObjectId() {
            return objectId;
        }
    }
}
