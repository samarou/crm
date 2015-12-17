package com.itechart.security.core;

import com.itechart.security.core.model.SecurityRole;
import com.itechart.security.core.model.SecurityUser;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.SecurityAcl;

import java.util.List;

/**
 * Repository provides data about security entities
 *
 * @author andrei.samarou
 */
public interface SecurityRepository {

    SecurityUser findUserByName(String userName);

    List<SecurityRole> getRoles();

    List<SecurityAcl> findAcls(ObjectIdentity oid);

    List<SecurityAcl> findAcls(List<ObjectIdentity> oids);

    Long getObjectTypeIdByName(String objectTypeName);
}