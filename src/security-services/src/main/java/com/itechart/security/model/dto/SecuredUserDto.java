package com.itechart.security.model.dto;

import com.itechart.security.model.persistent.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.itechart.security.util.Converter.convertCollection;

public class SecuredUserDto extends PublicUserDto {

    private String password;
    private boolean active;
    private Set<RoleDto> roles;
    private Set<GroupDto> groups;
    private List<UserDefaultAclEntryDto> acls;

    public SecuredUserDto() {
    }

    public SecuredUserDto(User entity) {
        super(entity);
        if (entity != null) {
            setActive(entity.isActive());
            setGroups(new HashSet<>(convertCollection(entity.getGroups(), GroupDto::new)));
            setRoles(new HashSet<>(convertCollection(entity.getRoles(), RoleDto::new)));
        }
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

    public Set<GroupDto> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDto> groups) {
        this.groups = groups;
    }

    public void setAcls(List<UserDefaultAclEntryDto> acls) {
        this.acls = acls;
    }

    public List<UserDefaultAclEntryDto> getAcls() {
        return acls;
    }

    public User convert() {
        User entity = super.convert();
        entity.setPassword(getPassword());
        entity.setActive(isActive());
        entity.setGroups(new HashSet<>(convertCollection(getGroups(), GroupDto::convert)));
        entity.setRoles(new HashSet<>(convertCollection(getRoles(), RoleDto::convert)));
        return entity;
    }
}
