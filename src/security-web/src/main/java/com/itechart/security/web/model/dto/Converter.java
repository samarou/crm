package com.itechart.security.web.model.dto;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.dto.AclEntryDto;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.User;

import java.util.HashSet;
import java.util.Set;

import static com.itechart.security.core.model.acl.Permission.*;

/**
 * Provides usefully methods to convert model to dto and vice versa.
 *
 * @author yauheni.putsykovich
 */
public class Converter {

    public static ContactFilter convert(ContactFilterDto dto) {
        ContactFilter filter = new ContactFilter();
        filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        return filter;
    }
    
    public static CompanyFilter convert(CompanyFilterDto dto) {
    	CompanyFilter filter = new CompanyFilter();
    	filter.setText(dto.getText());
        filter.setFrom(dto.getFrom());
        filter.setCount(dto.getCount());
        filter.setSortProperty(dto.getSortProperty());
        filter.setSortAsc(dto.isSortAsc());
        filter.setEmployeeNumberCategoryId(dto.getEmployeeNumberCategoryId());
        return filter;
    }

    public static Set<Permission> convert(AclEntryDto aclEntry) {
        Set<Permission> permissions = new HashSet<>();
        if (aclEntry.isCanRead()) permissions.add(READ);
        if (aclEntry.isCanWrite()) permissions.add(WRITE);
        if (aclEntry.isCanCreate()) permissions.add(CREATE);
        if (aclEntry.isCanDelete()) permissions.add(DELETE);
        if (aclEntry.isCanAdmin()) permissions.add(ADMIN);
        return permissions;
    }

    public static AclEntryDto convert(Principal principal, Set<Permission> permissions) {
        AclEntryDto dto = new AclEntryDto();
        initAcl(dto, principal, permissions);
        return dto;
    }

    private static void initAcl(AclEntryDto dto, Principal principal, Set<Permission> permissions) {
        dto.setPrincipalId(principal.getId());
        dto.setName(principal.getName());
        if (principal instanceof User) {
            dto.setPrincipalTypeName(com.itechart.security.model.PrincipalTypes.USER.getObjectType());
        } else if (principal instanceof Group) {
            dto.setPrincipalTypeName(com.itechart.security.model.PrincipalTypes.GROUP.getObjectType());
        }
        permissions.forEach(permission -> {
            if (permission == READ) dto.setCanRead(true);
            if (permission == WRITE) dto.setCanWrite(true);
            if (permission == CREATE) dto.setCanCreate(true);
            if (permission == DELETE) dto.setCanDelete(true);
            if (permission == ADMIN) dto.setCanAdmin(true);
        });
    }
}
