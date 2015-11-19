package com.itechart.sample.service.dao;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;

import java.util.List;

/**
 * @author andrei.samarou
 */
public interface AclDao extends BaseDao<Acl> {

    List<Long> findChildrenIds(Long parentId);

    Acl findByObjectKey(AclObjectKey key);

    List<Acl> findByObjectKeys(List<AclObjectKey> keys);
}
