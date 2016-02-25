package com.itechart.security.web.controller;

import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.service.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping("/privileges")
    public List<Privilege> getRoles() {
        return privilegeService.getPrivileges();
    }
}
