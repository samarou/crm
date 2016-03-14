package com.itechart.security.web.controller;

import com.itechart.security.business.filter.CustomerFilter;
import com.itechart.security.business.model.enums.ObjectTypes;
import com.itechart.security.business.service.CustomerService;
import com.itechart.security.core.SecurityUtils;
import com.itechart.security.core.acl.AclPermissionEvaluator;
import com.itechart.security.core.model.acl.ObjectIdentity;
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
import com.itechart.security.web.model.dto.CustomerFilterDto;
import com.itechart.security.web.model.dto.DataPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.itechart.security.core.model.acl.Permission.*;
import static com.itechart.security.web.model.dto.Converter.*;
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

    @Autowired
    private AclPermissionEvaluator aclPermissionEvaluator;

    @RequestMapping("/customers/{customerId}/permissions/delete")
    public boolean allowDeleting(@PathVariable Long customerId) {
        return aclPermissionEvaluator.hasPermission(SecurityUtils.getAuthentication(), getIdentity(customerId), DELETE);
    }

    @RequestMapping("/customers")
    public List<CustomerDto> getCustomers() {
        return convertCustomers(customerService.getCustomers());
    }

    @RequestMapping(value = "/customers", method = PUT)
    public void update(@RequestBody CustomerDto dto) {
        customerService.updateCustomer(covert(dto));
    }

    @RequestMapping("/customers/find")
    public DataPageDto find(CustomerFilterDto filterDto) {
        CustomerFilter filter = convert(filterDto);
        DataPageDto<CustomerDto> page = new DataPageDto<>();
        page.setData(convertCustomers(customerService.findCustomers(filter)));
        page.setTotalCount(customerService.countCustomers(filter));
        return page;
    }

    @RequestMapping(value = "/customers", method = POST)
    public Long create(@RequestBody CustomerDto dto) {
        Long customerId = customerService.saveCustomer(covert(dto));
        Long userId = SecurityUtils.getAuthenticatedUserId();
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

    private Acl getAcl(Long customerId) {
        return aclService.getAcl(getIdentity(customerId));
    }

    private ObjectIdentity getIdentity(Long customerId) {
        return new ObjectIdentityImpl(customerId, ObjectTypes.CUSTOMER.getName());
    }
}
