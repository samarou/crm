package com.itechart.sample.service.dao;

import com.itechart.sample.model.persistent.security.acl.Acl;

import java.io.Serializable;
import java.util.List;

/**
 * @author andrei.samarou
 */
public interface AclDao extends BaseDao<Acl> {

    List<Long> findChildrenIds(Long parentId);

    Acl findByObjectIdentity(Serializable objectId, Long objectTypeId);

}
