package com.itechart.security.web.model.dto;

/**
 * @author andrei.samarou
 */
public class SecuredUserFilterDto extends PublicUserFilterDto {
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
}
