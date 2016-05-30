package com.itechart.security.model.dto;

import com.itechart.security.model.filter.UserFilter;

public class SecuredUserFilterDto extends TextFilterDto {
    private Long groupId;
    private Long roleId;
    private boolean active;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public UserFilter convert() {
        UserFilter entity = super.convert(new UserFilter());
        entity.setGroupId(getGroupId());
        entity.setRoleId(getRoleId());
        entity.setActive(isActive());
        return entity;
    }
}
