package com.itechart.security.web.controller;

import com.itechart.security.model.dto.*;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.service.UserService;
import com.itechart.security.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

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
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/users/public")
    public List<PublicUserDto> getPublicUsers() {
        return userService.getPublicUsers();
    }

    @RequestMapping("/users/{id}")
    public SecuredUserDto findById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @RequestMapping(value = "/users", method = PUT)
    public void update(@RequestBody SecuredUserDto dto) {
        userService.updateUser(dto);
    }

    @RequestMapping(value = "/users", method = POST)
    public Long create(@RequestBody SecuredUserDto dto) {
        Long userId = userService.createUser(dto);
        List<String> roleNames = null;
        if (dto.getRoles() != null) {
            roleNames = dto.getRoles().stream()
                    .map(RoleDto::getName)
                    .collect(toList());
        }
        notificationService.sendUserCreatedNotification(dto.getEmail(), dto.getUserName(), roleNames);
        return userId;
    }

    @RequestMapping(value = "/users/activate/{id}", method = PUT)
    public void activate(@PathVariable Long id) {
        PublicUserDto user = userService.activateUser(id);
        if (user != null) {
            notificationService.sendUserActivatedNotification(user.getEmail(), user.getUserName());
        }
    }

    @RequestMapping(value = "/users/deactivate/{id}", method = PUT)
    public void deactivate(@PathVariable Long id) {
        PublicUserDto user = userService.deactivateUser(id);
        if (user != null) {
            notificationService.sendUserDeactivatedNotification(user.getEmail(), user.getUserName(), "unknown");
        }
    }

    @RequestMapping("/users/find")
    public DataPageDto<SecuredUserDto> find(SecuredUserFilterDto filterDto) {
        return userService.findUsers(filterDto);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/users/public/find")
    public DataPageDto<PublicUserDto> findPublicUsers(PublicUserFilterDto filterDto) {
        return userService.findPublicUsers(filterDto);
    }

    @RequestMapping(value = "/users/{userId}/acls/{principalId}", method = RequestMethod.DELETE)
    public void deleteAcl(@PathVariable Long userId, @PathVariable Long principalId) {
        userService.deleteAcl(userId, principalId);
    }

    @RequestMapping(value = "/users/{userId}/acls", method = GET)
    public List<UserDefaultAclEntryDto> getAcls(@PathVariable Long userId) {
        return userService.getAcls(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/users/current/acls", method = GET)
    public List<UserDefaultAclEntryDto> getDefaultAcls() {
        return userService.getDefaultAcls();
    }
}