package com.itechart.sample.service.acl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.persistent.security.acl.AclObjectKey;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Simple implementation of {@link AclCache} that delegates to EhCache
 *
 * @author andrei.samarou
 */
public class EhCacheBasedAclCache implements AclCache {

    private static final Logger logger = LoggerFactory.getLogger(EhCacheBasedAclCache.class);

    private Ehcache cache;

    @Override
    public Acl get(Serializable aclId) {
        Assert.notNull(aclId, "aclId required");
        Element element = null;
        try {
            element = cache.get(aclId);
        } catch (CacheException ce) {
            logger.warn(ce.getMessage(), ce);
        }
        if (element != null) {
            return (Acl) element.getObjectValue();
        }
        return null;
    }

    @Override
    public Acl get(AclObjectKey objectKey) {
        Assert.notNull(objectKey, "objectKey required");
        Element element = null;
        try {
            element = cache.get(objectKey);
        } catch (CacheException ce) {
            logger.warn(ce.getMessage(), ce);
        }
        if (element != null) {
            return (Acl) element.getObjectValue();
        }
        return null;
    }

    @Override
    public void put(Acl acl) {
        Assert.notNull(acl, "Acl required");
        Assert.notNull(acl.getObjectKey(), "Acl key required");
        cache.put(new Element(acl.getObjectKey(), acl));
        if (acl.getId() != null) {
            // case for fake empty ACL
            cache.put(new Element(acl.getId(), acl));
        }
    }

    @Override
    public void evict(Serializable aclId) {
        Assert.notNull(aclId, "aclId required");
        Acl acl = get(aclId);
        if (acl != null) {
            cache.remove(acl.getId());
            cache.remove(acl.getObjectKey());
        }
    }

    @Override
    public void evict(AclObjectKey objectKey) {
        Assert.notNull(objectKey, "objectKey required");
        Acl acl = get(objectKey);
        if (acl != null) {
            cache.remove(objectKey);
            if (acl.getId() != null) {
                // case for fake empty ACL
                cache.remove(acl.getId());
            }
        }
    }

    public void clearCache() {
        cache.removeAll();
    }

    @Required
    public void setCache(Ehcache cache) {
        this.cache = cache;
    }
}
