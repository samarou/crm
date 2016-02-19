package com.itechart.security.web.controller;

import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.service.UserService;
import com.itechart.security.web.model.dto.DataPageDto;
import com.itechart.security.web.model.dto.UserDto;
import com.itechart.security.web.model.dto.UserFilterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itechart.security.web.model.dto.Converter.convert;
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
    public DataPageDto<UserDto> find(UserFilterDto filterDto) {
        UserFilter filter = convert(filterDto);
        DataPageDto<UserDto> dataPage = new DataPageDto<>();
        dataPage.setData(convert(userService.findUsers(filter)));
        dataPage.setTotalCount(userService.countUsers(filter));
        return dataPage;
    }
}