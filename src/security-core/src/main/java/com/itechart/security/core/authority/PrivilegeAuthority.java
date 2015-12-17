package com.itechart.security.core.authority;

import com.itechart.security.core.model.SecurityPrivilege;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * PrivilegeAuthority represents user or role -granted privilege
 *
 * @author andrei.samarou
 */
public class PrivilegeAuthority implements GrantedAuthority {

    private String objectType;
    private String action;

    public PrivilegeAuthority(SecurityPrivilege privilege) {
        Assert.notNull(privilege);
        Assert.hasText(privilege.getObjectTypeName());
        Assert.hasText(privilege.getActionName());
        objectType = privilege.getObjectTypeName();
        action = privilege.getActionName();
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
