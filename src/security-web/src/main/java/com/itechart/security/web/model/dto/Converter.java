package com.itechart.security.web.model.dto;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.User;
import org.springframework.util.CollectionUtils;

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
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        return filter;
    }

    public static GroupDto convert(Group group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());

        return dto;
    }

    public static Group convert(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());

        return group;
    }

    //todo: have the same erasure with convert(List<User> groups) because of erase
    //maybe replace with Group... groups?
    public static List<GroupDto> convertGroups(List<Group> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.EMPTY_LIST;
        }
        return groups.stream().map(Converter::convert).collect(Collectors.toList());
    }
}
