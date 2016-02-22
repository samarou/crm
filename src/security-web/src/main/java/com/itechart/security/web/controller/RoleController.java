package com.itechart.security.web.controller;

import com.itechart.security.model.persistent.Role;
import com.itechart.security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }
}
