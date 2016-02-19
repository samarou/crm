package com.itechart.security.web.model.dto;

/**
 * @author andrei.samarou
 */
public class UserFilterDto extends PagingFilterDto {

    private String text;
    private Long groupId;
    private Long roleId;
    private boolean active;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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
