package com.itechart.security.web.model.dto;

import com.itechart.security.core.model.acl.Permission;

import java.util.Set;

/**
 * @author yauheni.putsykovich
 */
public class AccessToCustomerDto {
    private Long id;
    private String name;
    private String subObjectTypeName;
    private Set<Permission> permissions;

    public String getSubObjectTypeName() {
        return subObjectTypeName;
    }

    public void setSubObjectTypeName(String subObjectTypeName) {
        this.subObjectTypeName = subObjectTypeName;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
