package com.itechart.security.web.controller;

import com.itechart.security.business.service.CustomerService;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.web.model.SubObjectTypes;
import com.itechart.security.web.model.dto.AccessToCustomerDto;
import com.itechart.security.web.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.itechart.security.business.model.enums.ObjectTypes.CUSTOMER;
import static com.itechart.security.web.model.dto.Converter.convertCustomers;

/**
 * @author yauheni.putsykovich
 */
@RestController
@PreAuthorize("hasAnyRole('MANAGER', 'SPECIALIST')")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PrincipalService principalService;

    @Autowired
    private AclService aclService;

    @RequestMapping("/customers")
    public List<CustomerDto> getCustomers() {
        return convertCustomers(customerService.getCustomers());
    }

    @RequestMapping("/customer/{id}/rights")
    public List<AccessToCustomerDto> getRights(@PathVariable Long id){
        Acl acl = aclService.getAcl(new ObjectIdentityImpl(id, CUSTOMER.getName()));
        Map<Long, Set<Permission>> allPermissions = acl.getPermissions();
        List<Principal> principals = principalService.getByIds(new ArrayList<>(allPermissions.keySet()));
        List<AccessToCustomerDto> rights = principals.stream().map(principal -> {
            AccessToCustomerDto dto = new AccessToCustomerDto();
            dto.setId(principal.getId());
            dto.setName(principal.getName());
            dto.setPermissions(allPermissions.get(principal.getId()));
            if (principal instanceof User) {
                dto.setSubObjectTypeName(SubObjectTypes.USER.getName());
            } else if (principal instanceof Group) {
                dto.setSubObjectTypeName(SubObjectTypes.GROUP.getName());
            }
            return dto;
        }).collect(Collectors.toList());

        return rights;
    }
}
