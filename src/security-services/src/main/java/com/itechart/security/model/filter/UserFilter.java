package com.itechart.security.model.filter;

import com.itechart.common.model.filter.TextFilter;
/**
 * Filter for user data
 *
 * @author andrei.samarou
 */
public class UserFilter extends TextFilter {
    /**
     * User role ID
     */
    private Long roleId;
    /**
     * User group ID
     */
    private Long groupId;
    /**
     * Only active users
     */
    private boolean active;

    public UserFilter() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}