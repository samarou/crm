package com.itechart.security.model.dto;

import com.itechart.security.core.model.SecurityRole;
import java.util.Set;

public class RoleDto implements SecurityRole {
    private Long id;
    private String name;
    private String description;
    private RoleDto parent;
    private Set<PrivilegeDto> privileges;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleDto getParent() {
        return parent;
    }

    public void setParent(RoleDto parent) {
        this.parent = parent;
    }

    public Set<PrivilegeDto> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<PrivilegeDto> privileges) {
        this.privileges = privileges;
    }
}
