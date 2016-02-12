package com.itechart.security.web.controller;

import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.UserFilterDto;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("find")
    public List<UserDto> find(UserFilterDto filter) {
        return Collections.singletonList(new UserDto());
    }
}