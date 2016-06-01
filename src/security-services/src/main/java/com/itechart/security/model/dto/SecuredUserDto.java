package com.itechart.security.model.dto;

import java.util.List;
import java.util.Set;

public class SecuredUserDto extends PublicUserDto {

    private String password;
    private boolean active;
    private Set<RoleDto> roles;
    private Set<SecuredGroupDto> groups;
    private List<UserDefaultAclEntryDto> acls;

    public SecuredUserDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public Set<SecuredGroupDto> getGroups() {
        return groups;
    }

    public void setGroups(Set<SecuredGroupDto> groups) {
        this.groups = groups;
    }

    public void setAcls(List<UserDefaultAclEntryDto> acls) {
        this.acls = acls;
    }

    public List<UserDefaultAclEntryDto> getAcls() {
        return acls;
    }

}
