package com.itechart.security.web.controller;

import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.UserFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.itechart.security.web.model.dto.Converter.*;
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
        return convert(userService.getUsers());
    }

    @RequestMapping("/user/{id}")
    public UserDto findById(@PathVariable Long id) {
        return convert(userService.getUser(id));
    }

    @RequestMapping(value = "/user", method = PUT)
    public void update(@RequestBody UserDto dto) {
        userService.updateUser(convert(dto));
    }

    @RequestMapping(value = "/user", method = POST)
    public void create(@RequestBody UserDto dto) {
        userService.createUser(convert(dto));
    }

    @RequestMapping("/user/find")
    public List<UserDto> find(UserFilterDto dto) {
        return convert(userService.findUsers(convert(dto)));
    }
}