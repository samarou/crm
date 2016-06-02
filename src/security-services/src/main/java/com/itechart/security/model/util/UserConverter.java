package com.itechart.security.model.util;

import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.model.dto.SecuredUserDto;
import com.itechart.security.model.persistent.User;

import java.util.HashSet;
import java.util.Optional;

import static com.itechart.common.model.util.CollectionConverter.convertCollection;

public class UserConverter {

    public static PublicUserDto convertToPublicDto(User entity) {
        return Optional.ofNullable(entity).
                map(user -> populatePublicData(user, new PublicUserDto())).
                orElse(null);
    }

    private static <T extends PublicUserDto> T populatePublicData(User entity, T dto) {
        dto.setId(entity.getId());
        dto.setUserName(entity.getUserName());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        return dto;
    }

    public static SecuredUserDto convertToSecuredDto(User entity) {
        return Optional.ofNullable(entity).
                map(user -> populatePublicData(user, new SecuredUserDto())).
                map(dto -> populateSecuredData(entity, dto)).
                orElse(null);
    }

    private static <T extends SecuredUserDto> T populateSecuredData(User entity, T dto) {
        dto.setActive(entity.isActive());
        dto.setGroups(new HashSet<>(convertCollection(entity.getGroups(), GroupConverter::convertToSecuredDto)));
        dto.setRoles(new HashSet<>(convertCollection(entity.getRoles(), RoleConverter::convert)));
        return dto;
    }

    public static User convert(PublicUserDto dto) {
        User entity = new User();
        entity.setId(dto.getId());
        entity.setUserName(dto.getUserName());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        return entity;
    }

    public static User convert(SecuredUserDto dto) {
        User entity = convert((PublicUserDto) dto);
        entity.setPassword(dto.getPassword());
        entity.setActive(dto.isActive());
        entity.setGroups(new HashSet<>(convertCollection(dto.getGroups(), GroupConverter::convert)));
        entity.setRoles(new HashSet<>(convertCollection(dto.getRoles(), RoleConverter::convert)));
        return entity;
    }

}
