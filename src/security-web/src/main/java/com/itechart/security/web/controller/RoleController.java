package com.itechart.security.web.controller;

import com.itechart.security.service.RoleService;
import com.itechart.security.web.model.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itechart.security.web.model.dto.Converter.convert;
import static com.itechart.security.web.model.dto.Converter.convertRoles;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roles")
    public List<RoleDto> getRoles() {
        return convertRoles(roleService.getRoles());
    }

    @RequestMapping(value = "/roles", method = PUT)
    public void update(@RequestBody RoleDto role){
        roleService.updateRole(convert(role));
    }

    @RequestMapping(value = "/roles", method = POST)
    public void create(@RequestBody RoleDto role) {
        roleService.createRole(convert(role));
    }
}
