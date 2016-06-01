package com.itechart.security.model.util;

import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.PrincipalTypes;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.model.dto.UserDefaultAclEntryDto;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.UserDefaultAclEntry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itechart.security.core.model.acl.Permission.*;
import static com.itechart.security.core.model.acl.Permission.ADMIN;

public class AclConverter {

    public static AclEntryDto convert(Principal principal, Set<Permission> permissions) {
        AclEntryDto dto = new AclEntryDto();
        populateDto(principal, permissions, dto);
        return dto;
    }

    private static void populateDto(Principal principal, Set<Permission> permissions, AclEntryDto dto) {
        dto.setPrincipalId(principal.getId());
        dto.setName(principal.getName());
        if (principal instanceof User) {
            dto.setPrincipalTypeName(PrincipalTypes.USER.getObjectType());
        } else if (principal instanceof Group) {
            dto.setPrincipalTypeName(PrincipalTypes.GROUP.getObjectType());
        }
        permissions.forEach(permission -> {
            if (permission == READ) dto.setCanRead(true);
            if (permission == WRITE) dto.setCanWrite(true);
            if (permission == CREATE) dto.setCanCreate(true);
            if (permission == DELETE) dto.setCanDelete(true);
            if (permission == ADMIN) dto.setCanAdmin(true);
        });
    }

    public static Set<Permission> convert(AclEntryDto dto) {
        Set<Permission> permissions = new HashSet<>();
        if (dto.isCanRead()) permissions.add(READ);
        if (dto.isCanWrite()) permissions.add(WRITE);
        if (dto.isCanCreate()) permissions.add(CREATE);
        if (dto.isCanDelete()) permissions.add(DELETE);
        if (dto.isCanAdmin()) permissions.add(ADMIN);
        return permissions;
    }

    public static UserDefaultAclEntryDto convert(UserDefaultAclEntry entity) {
        UserDefaultAclEntryDto dto = new UserDefaultAclEntryDto();
        populateDto(entity.getPrincipal(), entity.getPermissions(), dto);
        dto.setId(entity.getId());
        return dto;
    }

    public static UserDefaultAclEntry convert(User user, List<Principal> principals, UserDefaultAclEntryDto dto) {
        UserDefaultAclEntry entity = new UserDefaultAclEntry();
        entity.setId(dto.getId());
        entity.setUser(user);
        entity.setPrincipal(findPrincipalById(principals, dto.getPrincipalId()));
        entity.setPermissions(convert(dto));
        return entity;
    }

    private static Principal findPrincipalById(List<Principal> principals, Long id) {
        return principals.stream().
                filter((p) -> p.getId().equals(id)).
                findAny().
                orElse(null);
    }
}
