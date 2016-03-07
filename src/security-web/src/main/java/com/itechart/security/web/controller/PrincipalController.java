package com.itechart.security.web.controller;

import com.itechart.security.service.AclService;
import com.itechart.security.web.model.dto.PrincipalDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yauheni.putsykovich
 */
@RestController
public class PrincipalController {

    private AclService aclService;

    @RequestMapping("/acl/customer/rights")
    public List<PrincipalDto> getPrincipals(Long id) {
        return null;
    }
}
