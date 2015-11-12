package com.itechart.sample.security.auth;

import com.itechart.sample.model.persistent.security.Group;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * GroupAuthority represents group that included user
 *
 * @author andrei.samarou
 */
public class GroupAuthority implements GrantedAuthority {

    private String group;

    public GroupAuthority(Group group) {
        Assert.notNull(group);
        Assert.hasText(group.getName());
        this.group = group.getName();
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String getAuthority() {
        return "GROUP:" + group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return group.equals(((GroupAuthority) o).group);
    }

    @Override
    public int hashCode() {
        return group.hashCode();
    }
}