package com.itechart.security.web.model.dto;

import static com.itechart.security.core.model.acl.Permission.ADMIN;
import static com.itechart.security.core.model.acl.Permission.CREATE;
import static com.itechart.security.core.model.acl.Permission.DELETE;
import static com.itechart.security.core.model.acl.Permission.READ;
import static com.itechart.security.core.model.acl.Permission.WRITE;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.itechart.security.business.filter.CompanyFilter;
import com.itechart.security.business.filter.ContactFilter;
import com.itechart.security.core.model.acl.Permission;
import com.itechart.security.model.filter.UserFilter;
import com.itechart.security.model.persistent.Action;
import com.itechart.security.model.persistent.Group;
import com.itechart.security.model.persistent.ObjectType;
import com.itechart.security.model.persistent.Principal;
import com.itechart.security.model.persistent.Privilege;
import com.itechart.security.model.persistent.Role;
import com.itechart.security.model.persistent.User;
import com.itechart.security.model.persistent.UserDefaultAclEntry;
import com.itechart.security.web.model.PrincipalTypes;

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
}
