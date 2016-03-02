package com.itechart.security.web.controller;

import com.itechart.security.service.PrivilegeService;
import com.itechart.security.web.model.dto.PrivilegeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.itechart.security.web.model.dto.Converter.convertPrivileges;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasRole('ADMIN')")
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping("/privileges")
    public List<PrivilegeDto> getPrivileges() {
        return convertPrivileges(privilegeService.getPrivileges());
    }
}
