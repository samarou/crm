package com.itechart.security.dao;

import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.model.persistent.acl.AclObjectKey;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface AclDao extends BaseDao<Acl> {

    List<Long> findChildrenIds(Long parentId);

    Acl findByObjectKey(AclObjectKey key);

    List<Acl> findByObjectKeys(List<AclObjectKey> keys);
}
