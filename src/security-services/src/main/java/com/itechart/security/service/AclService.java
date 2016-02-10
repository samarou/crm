package com.itechart.security.service;

import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.core.model.acl.ObjectIdentity;
import com.itechart.security.core.model.acl.Permission;

import java.util.List;
import java.util.Set;

/**
 * Service for managing of ACL entities
 *
 * @author andrei.samarou
 */
public interface AclService {

    Acl getAcl(Long aclId);

    Acl getAcl(ObjectIdentity objectIdentity);

    List<Acl> findAcls(List<ObjectIdentity> objectIdentities);

    List<Acl> findAclsWithAncestors(List<ObjectIdentity> objectIdentities);

    List<Acl> findAclWithAncestors(ObjectIdentity objectIdentity);

    Acl createAcl(ObjectIdentity objectIdentity, ObjectIdentity parentIdentity, Long ownerId);

    void updateAcl(Acl acl);

    void deleteAcl(Acl acl);
}
