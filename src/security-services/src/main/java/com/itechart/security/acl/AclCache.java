package com.itechart.security.acl;

import com.itechart.security.model.persistent.acl.Acl;
import com.itechart.security.model.persistent.acl.AclObjectKey;

import java.io.Serializable;

/**
 * A caching layer for {@link com.itechart.security.service.AclService}.
 *
 * @author andrei.samarou
 */
public interface AclCache {

    Acl get(Serializable aclId);

    Acl get(AclObjectKey objectKey);

    void put(Acl acl);

    void evict(Serializable aclId);

    void evict(AclObjectKey objectKey);

    void clearCache();
}