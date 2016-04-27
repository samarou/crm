package com.itechart.security.web.controller;

import com.itechart.security.core.SecurityUtils;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.Role;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.UserDefaultAclEntry;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.*;
import com.itechart.security.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.itechart.security.web.model.dto.Converter.convert;
import static com.itechart.security.web.model.dto.Converter.convertToDefaultAclDtos;
import static com.itechart.security.web.model.dto.Converter.convertToPublicUsers;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author andrei.samarou
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PrincipalService principalService;

    @RequestMapping("/users")
    public List<SecuredUserDto> findAll() {
        return convert(userService.getUsers());
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/users/public")
    public List<PublicUserDto> getPublicUsers() {
        return convertToPublicUsers(userService.getUsers());
    }

    @RequestMapping("/users/{id}")
    public SecuredUserDto findById(@PathVariable Long id) {
        return convert(userService.getUser(id));
    }

    @RequestMapping(value = "/users", method = PUT)
    public void update(@RequestBody SecuredUserDto dto) {
        User user = convert(dto);
        user.setAcls(getDefaultAcls(user, dto.getAcls()));
        userService.updateUser(user);
    }

    @RequestMapping(value = "/users", method = POST)
    public Long create(@RequestBody SecuredUserDto dto) {
        User user = convert(dto);
        user.setAcls(getDefaultAcls(user, dto.getAcls()));
        Long userId = userService.createUser(user);
        List<String> roleNames = null;
        if (user.getRoles() != null) {
            roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(toList());
        }
        notificationService.sendUserCreatedNotification(user.getEmail(), user.getUserName(), roleNames);
        return userId;
    }

    private List<UserDefaultAclEntry> getDefaultAcls(User user, List<UserDefaultAclEntryDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return Collections.emptyList();
        }
        List<Long> principalIds = dtos.stream().map(AclEntryDto::getId).collect(toList());
        List<Principal> principals = principalService.getByIds(principalIds);
        return dtos.stream().map(dto -> {
            UserDefaultAclEntry defaultAcl = new UserDefaultAclEntry();
            defaultAcl.setId(dto.getRecordId());
            defaultAcl.setUser(user);
            defaultAcl.setPrincipal(findPrincipalById(principals, dto.getId()));
            defaultAcl.setPermissions(convert(dto));
            return defaultAcl;
        }).collect(toList());
    }

    private Principal findPrincipalById(List<Principal> principals, Long id) {
        for (Principal principal : principals) {
            if (principal.getId().equals(id)) {
                return principal;
            }
        }
        return null;
    }

    @RequestMapping(value = "/users/activate/{id}", method = PUT)
    public void activate(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user != null) {
            userService.activateUser(id);
            notificationService.sendUserActivatedNotification(
                    user.getEmail(), user.getUserName());
        }
    }

    @RequestMapping(value = "/users/deactivate/{id}", method = PUT)
    public void deactivate(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user != null) {
            userService.deactivateUser(id);
            notificationService.sendUserDeactivatedNotification(
                    user.getEmail(), user.getUserName(), "unknown");
        }
    }

    @RequestMapping("/users/find")
    public DataPageDto<SecuredUserDto> find(SecuredUserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        DataPageDto<SecuredUserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convert(userService.findUsers(filter)));
        dataPage.setTotalCount(userService.countUsers(filter));
        return dataPage;
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/users/public/find")
    public DataPageDto<PublicUserDto> findPublicUsers(PublicUserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        filter.setActive(true);
        DataPageDto<PublicUserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convertToPublicUsers(userService.findUsers(filter)));
        dataPage.setTotalCount(userService.countUsers(filter));
        return dataPage;
    }

    @RequestMapping(value = "/users/{userId}/permissions/{principalId}", method = RequestMethod.DELETE)
    public void deletePermission(@PathVariable Long userId, @PathVariable Long principalId) {
        User user = userService.getUserWithAcls(userId);
        user.removeDefaultAcl(principalId);
        userService.updateUser(user);
    }

    @RequestMapping(value = "/users/{userId}/permissions", method = GET)
    public List<UserDefaultAclEntryDto> getPermission(@PathVariable Long userId) {
        User user = userService.getUserWithAcls(userId);
        return convertToDefaultAclDtos(user.getAcls());
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/users/current/permissions", method = GET)
    public List<UserDefaultAclEntryDto> getPermissionOfCurrentUser() {
        long userId = SecurityUtils.getAuthenticatedUserId();
        User user = userService.getUserWithAcls(userId);
        return convertToDefaultAclDtos(user.getAcls());
    }
}