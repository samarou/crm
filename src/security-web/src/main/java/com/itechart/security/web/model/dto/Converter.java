package com.itechart.security.web.model.dto;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.*;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Provides usefully methods to convert model to dto and vice versa.
 *
 * @author yauheni.putsykovich
 */
public class Converter {

    public static List<UserDto> convert(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.EMPTY_LIST;
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
        dto.setGroups(convertGroups(user.getGroups()));
        dto.setRoles(convertRoles(user.getRoles()));
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
        user.setGroups(convertGroupsDtos(dto.getGroups()));
        user.setRoles(convertRolesDto(dto.getRoles()));
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

    //todo: have the same erasure with convert(List<User> groups) because of erase
    //maybe replace with Group... groups?
    public static Set<GroupDto> convertGroups(Set<Group> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.EMPTY_SET;
        }
        return groups.stream().map(Converter::convert).collect(Collectors.toSet());
    }

    public static Set<Group> convertGroupsDtos(Set<GroupDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.EMPTY_SET;
        }
        return dtos.stream().map(Converter::convert).collect(Collectors.toSet());
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

    public static Set<RoleDto> convertRoles(Set<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.EMPTY_SET;
        }
        return roles.stream().map(Converter::convert).collect(Collectors.toSet());
    }

    public static Set<Role> convertRolesDto(Set<RoleDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.EMPTY_SET;
        }
        return dtos.stream().map(Converter::convert).collect(Collectors.toSet());
    }

    public static RoleDto convert(Role role) {
        if (role == null) return null;
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setParent(convert(role.getParent()));
        dto.setPrivileges(convertPrivileges(role.getPrivileges()));
        return dto;
    }

    public static Role convert(RoleDto dto) {
        if (dto == null) return null;
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setParent(convert(dto.getParent()));
        role.setPrivileges(convertPrivilegesDto(dto.getPrivileges()));
        return role;
    }

    public static Set<PrivilegeDto> convertPrivileges(Collection<Privilege> privileges) {
        if (CollectionUtils.isEmpty(privileges)) {
            return Collections.EMPTY_SET;
        }
        return privileges.stream().map(p -> {
            PrivilegeDto dto = new PrivilegeDto();
            dto.setId(p.getId());
            dto.setAction(convert(p.getAction()));
            dto.setObjectType(convert(p.getObjectType()));
            return dto;
        }).collect(Collectors.toSet());
    }

    public static Set<Privilege> convertPrivilegesDto(Collection<PrivilegeDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.EMPTY_SET;
        }
        return dtos.stream().map(dto -> {
            Privilege privilege = new Privilege();
            privilege.setId(dto.getId());
            privilege.setAction(convert(dto.getAction()));
            privilege.setObjectType(convert(dto.getObjectType()));
            return privilege;
        }).collect(Collectors.toSet());
    }

    public static ActionDto convert(Action action) {
        ActionDto dto = new ActionDto();
        dto.setId(action.getId());
        dto.setName(action.getName());
        dto.setDescription(action.getDescription());
        return dto;
    }

    public static Action convert(ActionDto dto) {
        Action action = new Action();
        action.setId(dto.getId());
        action.setName(dto.getName());
        action.setDescription(dto.getDescription());
        return action;
    }

    public static ObjectTypeDto convert(ObjectType objectType) {
        ObjectTypeDto dto = new ObjectTypeDto();
        dto.setId(objectType.getId());
        dto.setName(objectType.getName());
        dto.setDescription(objectType.getDescription());
        return dto;
    }

    public static ObjectType convert(ObjectTypeDto dto) {
        ObjectType objectType = new ObjectType();
        objectType.setId(dto.getId());
        objectType.setName(dto.getName());
        objectType.setDescription(dto.getDescription());
        return objectType;
    }
}
