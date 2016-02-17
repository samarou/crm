package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itechart.security.web.model.dto.util.Converter.*;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author andrei.samarou
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/users")
    public List<UserDto> findAll() {
        return toUserDtos(userService.findUsers(new UserFilter()));
    }

    @RequestMapping("/users/{id}")
    public UserDto findOne(@PathVariable Long id) {
        User user = userService.get(id);
        return toUserDto(user);
    }

    @RequestMapping(value = "/users", method = PUT)
    public void update(@RequestBody UserDto dto) {
        userService.updateUser(toUser(dto));
    }

    @RequestMapping(value = "/users", method = POST)
    public void create(@RequestBody UserDto dto) {
        userService.createUser(toUser(dto));
    }

    @RequestMapping("/users/find")
    public List<UserDto> find(@RequestParam(required = false) String text,
                              @RequestParam(required = false) Long groupId,
                              @RequestParam(required = false) Long roleId,
                              @RequestParam(required = false) boolean active) {
        return toUserDtos(userService.findUsers(new UserFilter(roleId, groupId, active, text)));
    }
}