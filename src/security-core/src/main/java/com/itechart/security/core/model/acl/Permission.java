package com.itechart.security.core.model.acl;

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

    public boolean isAllow(Permission another) {
        return ordinal() >= another.ordinal();
    }
}