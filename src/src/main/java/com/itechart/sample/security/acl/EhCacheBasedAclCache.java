package com.itechart.sample.security.acl;

import com.itechart.sample.model.persistent.security.acl.Acl;
import com.itechart.sample.model.security.ObjectIdentity;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Simple implementation of {@link AclCache} that delegates to EhCache
 *
 * @author andrei.samarou
 */
public class EhCacheBasedAclCache implements AclCache, InitializingBean {

    private static final Log logger = LogFactory.getLog(EhCacheBasedAclCache.class);

    private Ehcache cache;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "cache required");
    }

    @Override
    public Acl get(Serializable aclId) {
        Assert.notNull(aclId, "aclId required");
        Element element = null;
        try {
            element = cache.get(aclId);
        } catch (CacheException ce) {
            logger.warn(ce);
        }
        if (element != null) {
            return (Acl) element.getObjectValue();
        }
        return null;
    }

    @Override
    public Acl get(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "objectIdentity required");
        Element element = null;
        try {
            element = cache.get(new ObjectIdentityKey(objectIdentity));
        } catch (CacheException ce) {
            logger.warn(ce);
        }
        if (element != null) {
            return (Acl) element.getObjectValue();
        }
        return null;
    }

    @Override
    public void put(Acl acl) {
        Assert.notNull(acl, "Acl required");
        Assert.notNull(acl.getId(), "Acl id required");
        if (acl.getParent() != null) {
            put(acl.getParent());
        }
        cache.put(new Element(acl.getId(), acl));
        cache.put(new Element(new ObjectIdentityKey(acl), acl));
    }

    @Override
    public void evict(Serializable aclId) {
        Assert.notNull(aclId, "aclId required");
        Acl acl = get(aclId);
        if (acl != null) {
            cache.remove(acl.getId());
            cache.remove(new ObjectIdentityKey(acl));
        }
    }

    @Override
    public void evict(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "objectIdentity required");
        Acl acl = get(objectIdentity);
        if (acl != null) {
            cache.remove(acl.getId());
            cache.remove(objectIdentity);
        }
    }

    public void clearCache() {
        cache.removeAll();
    }

    @Required
    public void setCache(Ehcache cache) {
        this.cache = cache;
    }

    /**
     * Wrapping key for ObjectIdentity
     */
    private static class ObjectIdentityKey {
        private Serializable id;
        private int type;

        public ObjectIdentityKey(ObjectIdentity objectIdentity) {
            this(objectIdentity.getId(), objectIdentity.getType());
        }

        public ObjectIdentityKey(Acl acl) {
            this(acl.getObjectId(), acl.getObjectType().getName());
        }

        public ObjectIdentityKey(Serializable objectId, String objectType) {
            Assert.notNull(objectId);
            Assert.notNull(objectType);
            this.id = objectId;
            this.type = objectType.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ObjectIdentityKey that = (ObjectIdentityKey) o;
            return id.equals(that.id) && type == that.type;
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + type;
            return result;
        }
    }

}
