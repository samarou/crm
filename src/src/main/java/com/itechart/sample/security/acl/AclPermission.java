package com.itechart.sample.security.acl;

import org.springframework.security.acls.domain.BasePermission;

/**
 * Set of additional permissions
 *
 * @author andrei.samarou
 */
public class AclPermission extends BasePermission {

    protected AclPermission(int mask, char code) {
        super(mask, code);
    }
}
