package com.itechart.security.core.model.acl;

import java.util.EnumSet;
import java.util.Set;

/**
 * ACL permission
 *
 * @author andrei.samarou
 */
public enum Permission {
    READ,   // 1
    WRITE,  // 2
    CREATE, // 4
    DELETE, // 8
    ADMIN;  // 16

    public int getMask() {
        return 1 << ordinal();
    }

    public static Set<Permission> asSet(int mask) {
        int sum = 0;
        Set<Permission> result = EnumSet.noneOf(Permission.class);
        for (Permission permission : Permission.values()) {
            sum |= permission.getMask();
            if(mask >= sum) result.add(permission);
        }
        return result;
    }

    public boolean isAllow(Permission another) {
        return ordinal() >= another.ordinal();
    }
}