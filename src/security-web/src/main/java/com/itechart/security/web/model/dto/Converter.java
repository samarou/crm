package com.itechart.security.web.model.dto;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides usefully methods to convert model to dto and vice versa.
 *
 * @author yauheni.putsykovich
 */
public class Converter {

    public static List<UserDto> convert(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream().map(Converter::convert).collect(Collectors.toList());
    }

    public static UserDto convert(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());
        dto.setGroups(user.getGroups());
        dto.setRoles(user.getRoles());
        return dto;
    }

    public static User convert(UserDto dto) {
        if (dto == null) {
            return null;
        }
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

    public static UserFilter convert(UserFilterDto dto) {
        UserFilter filter = new UserFilter();
        filter.setRoleId(dto.getRoleId());
        filter.setGroupId(dto.getGroupId());
        filter.setActive(dto.isActive());
        filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        return filter;
    }
}
