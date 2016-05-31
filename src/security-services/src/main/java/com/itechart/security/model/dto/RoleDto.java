package com.itechart.security.model.dto;

import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.model.persistent.Role;

import java.util.HashSet;
import java.util.Set;

import static com.itechart.common.model.util.Converter.convertCollection;

public class RoleDto implements SecurityRole {
    private Long id;
    private String name;
    private String description;
    private RoleDto parent;
    private Set<PrivilegeDto> privileges;

    public RoleDto() {
    }

    public RoleDto(Role entity) {
        if (entity != null) {
            setId(entity.getId());
            setName(entity.getName());
            setDescription(entity.getDescription());
            setParent(new RoleDto(entity.getParent()));
            setPrivileges(new HashSet<>(convertCollection(entity.getPrivileges(), PrivilegeDto::new)));
        }
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

    public Role convert() {
        Role entity = new Role();
        entity.setId(getId());
        entity.setName(getName());
        entity.setDescription(getDescription());
        entity.setParent(getParent() != null ? getParent().convert() : null);
        entity.setPrivileges(new HashSet<>(convertCollection(getPrivileges(), PrivilegeDto::convert)));
        return entity;
    }
}
