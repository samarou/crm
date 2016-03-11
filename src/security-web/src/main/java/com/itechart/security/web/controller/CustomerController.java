package com.itechart.security.web.controller;

import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.CustomerService;
import com.itechart.security.core.model.acl.ObjectIdentityImpl;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.core.userdetails.UserDetailsImpl;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.service.AclService;
import com.itechart.security.service.PrincipalService;
import com.itechart.security.web.model.PrincipalTypes;
import com.itechart.security.web.model.dto.AclEntryDto;
import com.itechart.security.web.model.dto.CustomerDto;
import com.itechart.security.web.security.token.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/customers", method = POST)
    public Long create(@RequestBody CustomerDto dto) {
        Long customerId = customerService.saveCustomer(covert(dto));
        TokenAuthentication token = (TokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((UserDetailsImpl) token.getPrincipal()).getUserId();
        aclService.createAcl(new ObjectIdentityImpl(customerId, ObjectTypes.CUSTOMER.getName()), null, userId);
        return customerId;
    }

    @RequestMapping(value = "/customers/{customerId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long customerId) {
        Acl acl = getAcl(customerId);
        aclService.deleteAcl(acl);
        customerService.deleteById(customerId);
    }

    @RequestMapping("/customers/{customerId}/permissions")
    public List<AclEntryDto> getPermissions(@PathVariable Long customerId) {
        Acl acl = getAcl(customerId);
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

    @RequestMapping(value = "/customers/{customerId}/permissions", method = PUT)
    public void createOrUpdatePermissions(@PathVariable Long customerId, @RequestBody List<AclEntryDto> permissions) {
        Acl acl = getAcl(customerId);
        permissions.forEach(permission -> {
            boolean isItNewPrincipal = acl.getPermissions(permission.getId()) == null;
            if (isItNewPrincipal) acl.addPermissions(permission.getId(), Collections.emptySet());

            acl.denyAll(permission.getId());
            if (permission.isCanRead()) acl.addPermission(permission.getId(), READ);
            if (permission.isCanWrite()) acl.addPermission(permission.getId(), WRITE);
            if (permission.isCanCreate()) acl.addPermission(permission.getId(), CREATE);
            if (permission.isCanDelete()) acl.addPermission(permission.getId(), DELETE);
            if (permission.isCanAdmin()) acl.addPermission(permission.getId(), ADMIN);
        });
        aclService.updateAcl(acl);
    }

    @RequestMapping(value = "/customers/{customerId}/permissions/{principalId}", method = RequestMethod.DELETE)
    public void deletePermission(@PathVariable Long customerId, @PathVariable Long principalId) {
        Acl acl = getAcl(customerId);
        acl.removePrincipal(principalId);
        aclService.updateAcl(acl);
    }

    private Acl getAcl(Long customerId){
        return aclService.getAcl(new ObjectIdentityImpl(customerId, ObjectTypes.CUSTOMER.getName()));
    }
}
