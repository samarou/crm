package com.itechart.security.web.model.dto.util;

import com.itechart.security.model.persistent.User;
import com.itechart.security.web.model.dto.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides usefully methods to convert model to dto and vice versa.
 *
 * @author yauheni.putsykovich
 */
public class Converter {
    public static List<UserDto> toUserDtoList(List<User> users) {
        if (users.isEmpty()) return Collections.emptyList();
        return users.stream().map(Converter::toUserDto).collect(Collectors.toList());
    }

    public static UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());
        dto.setGroups(user.getGroups());
        dto.setRoles(user.getRoles());
        return dto;
    }

    public static User toUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setActive(dto.isActive());
        user.setGroups(dto.getGroups());
        user.setRoles(dto.getRoles());
        return user;
    }
}
