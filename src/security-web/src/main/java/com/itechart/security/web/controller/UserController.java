package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author andrei.samarou
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public List<UserDto> findAll() {
        List<User> users = userService.findUsers(new UserFilter());
        return users != null ? Converter.toUserDtoList(users) : Collections.emptyList();
    }

    @RequestMapping("/user/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.get(id);
        return user != null ? Converter.toUserDto(user) : null;
    }

    @RequestMapping(value = "/user", method = PUT)
    public void update(@RequestBody UserDto dto) {
        User user = Converter.toUser(dto);
        userService.updateUser(user);
    }

    @RequestMapping(value = "/user", method = POST)
    public void create(@RequestBody UserDto dto) {
        User user = Converter.toUser(dto);
        userService.createUser(user);
    }
}