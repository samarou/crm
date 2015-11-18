package com.itechart.sample.model.security;

/**
 * Permission
 *
 * @author andrei.samarou
 */
public enum Permission {
    READ,
    WRITE,
    CREATE,
    DELETE,
    ADMIN;

    public int getMask() {
        return 1 << ordinal();
    }

    public boolean isAllow(Permission another) {
        return ordinal() >= another.ordinal();
    }
}