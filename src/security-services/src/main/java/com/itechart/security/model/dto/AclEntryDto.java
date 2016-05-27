package com.itechart.security.model.dto;

import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.PrincipalTypes;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;

import java.util.HashSet;
import java.util.Set;

import static com.itechart.security.core.model.acl.Permission.*;
import static com.itechart.security.core.model.acl.Permission.ADMIN;

public class AclEntryDto {
    private Long principalId;
    private String name;
    private String principalTypeName;
    private boolean canRead;
    private boolean canWrite;
    private boolean canCreate;
    private boolean canDelete;
    private boolean canAdmin;

    public AclEntryDto() {
    }

    public AclEntryDto(Principal principal, Set<Permission> permissions) {
        setPrincipalId(principal.getId());
        setName(principal.getName());
        if (principal instanceof User) {
            setPrincipalTypeName(PrincipalTypes.USER.getObjectType());
        } else if (principal instanceof Group) {
            setPrincipalTypeName(PrincipalTypes.GROUP.getObjectType());
        }
        permissions.forEach(permission -> {
            if (permission == READ) setCanRead(true);
            if (permission == WRITE) setCanWrite(true);
            if (permission == CREATE) setCanCreate(true);
            if (permission == DELETE) setCanDelete(true);
            if (permission == ADMIN) setCanAdmin(true);
        });
    }

    public Long getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrincipalTypeName() {
        return principalTypeName;
    }

    public void setPrincipalTypeName(String principalTypeName) {
        this.principalTypeName = principalTypeName;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanAdmin() {
        return canAdmin;
    }

    public void setCanAdmin(boolean canAdmin) {
        this.canAdmin = canAdmin;
    }

    public Set<Permission> convert() {
        Set<Permission> permissions = new HashSet<>();
        if (isCanRead()) permissions.add(READ);
        if (isCanWrite()) permissions.add(WRITE);
        if (isCanCreate()) permissions.add(CREATE);
        if (isCanDelete()) permissions.add(DELETE);
        if (isCanAdmin()) permissions.add(ADMIN);
        return permissions;
    }
}

