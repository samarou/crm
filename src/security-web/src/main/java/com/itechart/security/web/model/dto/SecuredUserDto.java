package com.itechart.security.web.model.dto;

import java.util.Set;

/**
 * @author andrei.samarou
 */
public class SecuredUserDto extends PublicUserDto {
    private Set<RoleDto> roles;
    private Set<GroupDto> groups;

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public Set<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDto> groups) {
        this.groups = groups;
    }
}
