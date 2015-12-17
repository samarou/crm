package com.itechart.security.core.model.acl;

import java.io.Serializable;
import java.util.Set;

/**
 * Security ACL data
 *
 * @author andrei.samarou
 */
public interface SecurityAcl {

    Long getId();

    Serializable getObjectKey();

    Long getOwnerId();

    Set<Permission> getPermissions(Long principalId);
}