package com.itechart.security.web.controller;

import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.CustomerService;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.web.model.PrincipalTypes;
import com.itechart.security.web.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static com.itechart.security.core.model.acl.Permission.*;
import static com.itechart.security.web.model.dto.Converter.convertCustomers;
import static com.itechart.security.web.model.dto.Converter.covert;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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

    @RequestMapping(value = "/customers", method = PUT)
    public void update(@RequestBody CustomerDto dto) {
        customerService.updateCustomer(covert(dto));
    }

    @RequestMapping("/customers/{id}/permissions")
    public List<AclEntryDto> getPermissions(@PathVariable Long id) {
        Acl acl = aclService.getAcl(new ObjectIdentityImpl(id, ObjectTypes.CUSTOMER.getName()));
        Map<Long, Set<Permission>> allPermissions = acl.getPermissions();
        List<Principal> principals = principalService.getByIds(new ArrayList<>(allPermissions.keySet()));
        return principals.stream().map(principal -> {
            AclEntryDto dto = new AclEntryDto();
            dto.setId(principal.getId());
            dto.setName(principal.getName());
            allPermissions.get(principal.getId()).forEach(permission -> {
                dto.setCanRead(permission == READ);
                dto.setCanWrite(permission == WRITE);
                dto.setCanCreate(permission == CREATE);
                dto.setCanDelete(permission == DELETE);
                dto.setCanAdmin(permission == ADMIN);
            });
            if (principal instanceof User) {
                dto.setPrincipalTypeName(PrincipalTypes.USER.getObjectType());
            } else if (principal instanceof Group) {
                dto.setPrincipalTypeName(PrincipalTypes.GROUP.getObjectType());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/customers/{id}/permissions", method = PUT)
    public void updatePermissions(@PathVariable Long id, @RequestBody List<AclEntryDto> permissions) {
        Acl acl = aclService.getAcl(new ObjectIdentityImpl(id, ObjectTypes.CUSTOMER.getName()));
        permissions.forEach(permission -> {
            acl.denyAll(permission.getId());
            if (permission.isCanRead()) acl.addPermission(permission.getId(), READ);
            if (permission.isCanWrite()) acl.addPermission(permission.getId(), WRITE);
            if (permission.isCanCreate()) acl.addPermission(permission.getId(), CREATE);
            if (permission.isCanDelete()) acl.addPermission(permission.getId(), DELETE);
            if (permission.isCanAdmin()) acl.addPermission(permission.getId(), ADMIN);
        });
        aclService.updateAcl(acl);
    }

    @RequestMapping(value = "/customers/{id}/permissions", method = POST)
    public void addPermissions(@PathVariable Long id, @RequestBody List<Long> principal) {
        principal.forEach(principalId -> {
            Acl acl = aclService.getAcl(new ObjectIdentityImpl(id, ObjectTypes.CUSTOMER.getName()));
            acl.addPermissions(principalId, Collections.emptySet());
            aclService.updateAcl(acl);
        });
    }
}
