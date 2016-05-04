package com.itechart.security.web.model.dto;

import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.business.model.dto.ContactDto;
import com.itechart.security.business.model.dto.OrderDto;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.Order;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides usefully methods to convert model to dto and vice versa.
 *
 * @author yauheni.putsykovich
 */
public class Converter {
    public static List<SecuredUserDto> convert(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(Converter::convert).collect(Collectors.toList());
    }

    public static SecuredUserDto convert(User user) {
        if (user == null) {
            return null;
        }
        SecuredUserDto dto = new SecuredUserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setActive(user.isActive());
        dto.setGroups(new HashSet<>(convertGroups(new ArrayList<>(user.getGroups()))));
        dto.setRoles(new HashSet<>(convertRoles(new ArrayList<>(user.getRoles()))));
        return dto;
    }

    public static User convert(SecuredUserDto dto) {
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
        user.setGroups(new HashSet<>(convertGroupsDtos(new ArrayList<>(dto.getGroups()))));
        user.setRoles(new HashSet<>(convertRolesDto(new ArrayList<>(dto.getRoles()))));
        return user;
    }

    public static List<PublicUserDto> convertToPublicUsers(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(Converter::convertToPublicUser).collect(Collectors.toList());
    }

    private static PublicUserDto convertToPublicUser(User user) {
        if (user == null) {
            return null;
        }
        PublicUserDto dto = new PublicUserDto();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }

    public static UserFilter convert(SecuredUserFilterDto dto) {
        UserFilter filter = new UserFilter();
        filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        filter.setRoleId(dto.getRoleId());
        filter.setGroupId(dto.getGroupId());
        filter.setActive(dto.isActive());
        return filter;
    }

    public static UserFilter convert(PublicUserFilterDto dto) {
        UserFilter filter = new UserFilter();
        filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        return filter;
    }

    public static ContactFilter convert(ContactFilterDto dto) {
        ContactFilter filter = new ContactFilter();
        filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        return filter;
    }

    //todo: have the same erasure with convert(List<User> groups) because of erase
    //maybe replace with Group... groups?
    public static List<GroupDto> convertGroups(List<Group> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        return groups.stream().map(Converter::convert).collect(Collectors.toList());
    }

    public static List<Group> convertGroupsDtos(List<GroupDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(Converter::convert).collect(Collectors.toList());
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

    public static List<PublicGroupDto> convertToPublicGroups(List<Group> groups) {
        if (CollectionUtils.isEmpty(groups)) {
            return Collections.emptyList();
        }
        return groups.stream().map(Converter::convertToPublicGroup).collect(Collectors.toList());
    }

    public static PublicGroupDto convertToPublicGroup(Group group) {
        PublicGroupDto dto = new PublicGroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());

        return dto;
    }

    public static List<RoleDto> convertRoles(List<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }
        return roles.stream().map(Converter::convert).collect(Collectors.toList());
    }

    public static List<Role> convertRolesDto(List<RoleDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        return dtos.stream().map(Converter::convert).collect(Collectors.toList());
    }

    public static RoleDto convert(Role role) {
        if (role == null) return null;
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setParent(convert(role.getParent()));
        dto.setPrivileges(new HashSet<>(convertPrivileges(role.getPrivileges())));
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

    public static List<PrivilegeDto> convertPrivileges(Collection<Privilege> privileges) {
        if (CollectionUtils.isEmpty(privileges)) {
            return Collections.emptyList();
        }
        return privileges.stream().map(p -> {
            PrivilegeDto dto = new PrivilegeDto();
            dto.setId(p.getId());
            dto.setAction(convert(p.getAction()));
            dto.setObjectType(convert(p.getObjectType()));
            return dto;
        }).collect(Collectors.toList());
    }

    public static Set<Privilege> convertPrivilegesDto(Collection<PrivilegeDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptySet();
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
