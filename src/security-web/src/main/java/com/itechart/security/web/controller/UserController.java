package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.Role;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.DataPageDto;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.UserFilterDto;
import com.itechart.security.web.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.itechart.security.web.model.dto.Converter.convert;
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

    @RequestMapping("/users")
    public List<UserDto> findAll() {
        return convert(userService.getUsers());
    }

    @RequestMapping("/users/{id}")
    public UserDto findById(@PathVariable Long id) {
        return convert(userService.getUser(id));
    }

    @RequestMapping(value = "/users", method = PUT)
    public void update(@RequestBody UserDto dto) {
        userService.updateUser(convert(dto));
    }

    @RequestMapping(value = "/users", method = POST)
    public void create(@RequestBody UserDto dto) {
        User user = convert(dto);
        userService.createUser(user);
        List<String> roleNames = null;
        if (user.getRoles() != null) {
            roleNames = user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());
        }
        notificationService.sendUserCreatedNotification(
                user.getEmail(), user.getUserName(), roleNames);
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
    public DataPageDto<UserDto> find(UserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        DataPageDto<UserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convert(userService.findUsers(filter)));
        dataPage.setTotalCount(userService.countUsers(filter));
        return dataPage;
    }
}