package com.itechart.sample.security.auth;

import com.itechart.sample.model.persistent.security.Privilege;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * PrivilegeAuthority represents user or role -granted privelege
 *
 * @author andrei.samarou
 */
public class PrivilegeAuthority implements GrantedAuthority {

    private String objectType;
    private String action;

    public PrivilegeAuthority(Privilege privilege) {
        Assert.notNull(privilege);
        Assert.hasText(privilege.getObjectType().getName());
        Assert.hasText(privilege.getAction().getName());
        objectType = privilege.getObjectType().getName();
        action = privilege.getAction().getName();
    }

    public String getObjectType() {
        return objectType;
    }

    public String getAction() {
        return action;
    }

    @Override
    public String getAuthority() {
        return "PRIV:" + objectType + ":" + action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrivilegeAuthority that = (PrivilegeAuthority) o;
        return objectType.equals(that.objectType) && action.equals(that.action);
    }

    @Override
    public int hashCode() {
        int result = objectType.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }
}
