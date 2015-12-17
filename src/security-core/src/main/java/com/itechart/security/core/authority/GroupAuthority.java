package com.itechart.security.core.authority;

import com.itechart.security.core.model.SecurityGroup;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * GroupAuthority represents group that included user
 *
 * @author andrei.samarou
 */
public class GroupAuthority implements GrantedAuthority {

    private Long groupId;
    private String groupName;

    public GroupAuthority(SecurityGroup group) {
        Assert.notNull(group);
        Assert.notNull(group.getId());
        Assert.hasText(group.getName());
        this.groupId = group.getId();
        this.groupName = group.getName();
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getAuthority() {
        return "GROUP:" + groupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupAuthority that = (GroupAuthority) o;
        return groupId.equals(that.groupId) && groupName.equals(that.groupName);
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + groupName.hashCode();
        return result;
    }
}