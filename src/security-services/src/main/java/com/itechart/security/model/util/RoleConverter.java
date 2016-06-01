package com.itechart.security.model.util;

import com.itechart.security.model.dto.RoleDto;
import com.itechart.security.model.persistent.Role;

import java.util.HashSet;
import java.util.Optional;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;

public class RoleConverter {

    public static RoleDto convert(Role entity) {
        return Optional.ofNullable(entity).map((role) -> {
            RoleDto dto = new RoleDto();
            dto.setId(role.getId());
            dto.setName(role.getName());
            dto.setDescription(role.getDescription());
            dto.setParent(convert(role.getParent()));
            dto.setPrivileges(new HashSet<>(convertCollection(role.getPrivileges(), PrivilegeConverter::convert)));
            return dto;
        }).orElse(null);
    }

    public static Role convert(RoleDto dto) {
        return Optional.ofNullable(dto).map((roleDto) -> {
            Role entity = new Role();
            entity.setId(dto.getId());
            entity.setName(dto.getName());
            entity.setDescription(dto.getDescription());
            entity.setParent(convert(dto.getParent()));
            entity.setPrivileges(new HashSet<>(convertCollection(dto.getPrivileges(), PrivilegeConverter::convert)));
            return entity;
        }).orElse(null);
    }

}
