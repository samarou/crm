package com.itechart.sample.security.acl;


import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.security.ObjectIdentity;
import com.itechart.sample.service.AclService;

import java.io.Serializable;

/**
 * A caching layer for {@link AclService}.
 *
 * @author andrei.samarou
 */
public interface AclCache {

    Acl get(Serializable aclId);

    Acl get(ObjectIdentity objectIdentity);

    void put(Acl acl);

    void evict(Serializable aclId);

    void evict(ObjectIdentity objectIdentity);

    void clearCache();

}
