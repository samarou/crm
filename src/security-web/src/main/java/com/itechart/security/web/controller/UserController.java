package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.User;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

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
        if (users != null) {
            return Converter.toUserDtoList(users);
        }
        return Collections.emptyList();
    }
}