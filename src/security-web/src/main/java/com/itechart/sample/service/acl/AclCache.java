package com.itechart.sample.service.acl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;

import java.io.Serializable;

/**
 * A caching layer for {@link com.itechart.sample.service.AclService}.
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