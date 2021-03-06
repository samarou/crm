package com.itechart.security.web.controller;

import com.itechart.security.service.RoleService;
import com.itechart.security.model.dto.RoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequestMapping("/roles")
    public List<RoleDto> getRoles() {
        return roleService.getRoles();
    }

    @RequestMapping(value = "/roles/{id}", method = GET)
    public RoleDto get(@PathVariable Long id){
        return roleService.getRole(id);
    }

    @RequestMapping(value = "/roles", method = PUT)
    public void update(@RequestBody RoleDto role){
        roleService.updateRole(role);
    }

    @RequestMapping(value = "/roles", method = POST)
    public Long create(@RequestBody RoleDto role) {
        return roleService.createRole(role);
    }

    @RequestMapping(value = "/roles/{id}", method = DELETE)
    public void delete(@PathVariable Long id){
        roleService.deleteRoleById(id);
    }
}
