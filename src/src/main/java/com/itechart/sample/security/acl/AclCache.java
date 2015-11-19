package com.itechart.sample.security.acl;


import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;
import com.itechart.sample.service.AclService;

import java.io.Serializable;

/**
 * A caching layer for {@link AclService}.
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