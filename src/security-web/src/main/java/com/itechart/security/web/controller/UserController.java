package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author andrei.samarou
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/users")
    public List<UserDto> findAll() {
        List<User> users = userService.findUsers(new UserFilter());
        if (users != null) return Converter.toUserDtoList(users);
        return Collections.emptyList();
    }

    @ResponseBody
    @RequestMapping("/users/{name}")
    public UserDto findOne(@PathVariable String name) {
        User user = userService.findByName(name);
        if (user != null) return Converter.toUserDto(user);
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/users", method = PUT)
    public void update(@RequestBody UserDto dto) {
        User user = Converter.toUser(dto);
        userService.updateUser(user);
    }

    @ResponseBody
    @RequestMapping(value = "/users", method = POST)
    public void create(@RequestBody UserDto dto) {
        User user = Converter.toUser(dto);
        userService.createUser(user);
    }
}