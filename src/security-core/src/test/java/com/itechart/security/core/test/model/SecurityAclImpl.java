package com.itechart.security.core.test.model;

import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.model.acl.SecurityAcl;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link SecurityAcl} for test purposes
 *
 * @author andrei.samarou
 */
public class SecurityAclImpl implements SecurityAcl, Serializable {

    private Serializable objectKey;
    private Long ownerId;

    private Map<Long, Set<Permission>> permissions = new HashMap<>();

    public SecurityAclImpl(Serializable objectKey, Long ownerId) {
        this.objectKey = objectKey;
        this.ownerId = ownerId;
    }

    @Override
    public Long getId() {
        return (long) objectKey.hashCode();
    }

    @Override
    public Serializable getObjectKey() {
        return objectKey;
    }

    @Override
    public Long getOwnerId() {
        return ownerId;
    }

    @Override
    public Set<Permission> getPermissions(Long principalId) {
        return permissions.get(principalId);
    }

    public void addPermissions(long principalId, Set<Permission> permissions) {
        Set<Permission> principalPermissions =
                this.permissions.computeIfAbsent(principalId, s -> EnumSet.noneOf(Permission.class));
        principalPermissions.addAll(permissions);
    }
}
