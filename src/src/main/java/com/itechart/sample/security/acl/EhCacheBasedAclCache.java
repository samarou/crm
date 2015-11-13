package com.itechart.sample.security.acl;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Simple implementation of {@link AclCache} that delegates to EhCache
 *
 * @author andrei.samarou
 */
public class EhCacheBasedAclCache implements AclCache, InitializingBean {

    private Ehcache cache;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cache, "cache required");
    }

    public void evictFromCache(Serializable id) {
        Assert.notNull(id, "Primary key (identifier) required");

        MutableAcl acl = getFromCache(id);

        if (acl != null) {
            cache.remove(acl.getId());
            cache.remove(acl.getObjectIdentity());
        }
    }

    public void evictFromCache(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "ObjectIdentity required");

        MutableAcl acl = getFromCache(objectIdentity);

        if (acl != null) {
            cache.remove(acl.getId());
            cache.remove(acl.getObjectIdentity());
        }
    }

    public MutableAcl getFromCache(ObjectIdentity objectIdentity) {
        Assert.notNull(objectIdentity, "ObjectIdentity required");

        Element element = null;

        try {
            element = cache.get(objectIdentity);
        }
        catch (CacheException ignored) {
        }

        if (element == null) {
            return null;
        }

        return initializeTransientFields((MutableAcl) element.getValue());
    }

    public MutableAcl getFromCache(Serializable id) {
        Assert.notNull(id, "Primary key (identifier) required");

        Element element = null;

        try {
            element = cache.get(id);
        }
        catch (CacheException ignored) {
        }

        if (element == null) {
            return null;
        }

        return initializeTransientFields((MutableAcl) element.getValue());
    }

    public void putInCache(MutableAcl acl) {
        Assert.notNull(acl, "Acl required");
        Assert.notNull(acl.getObjectIdentity(), "ObjectIdentity required");
        Assert.notNull(acl.getId(), "ID required");



        if ((acl.getParentAcl() != null) && (acl.getParentAcl() instanceof MutableAcl)) {
            putInCache((MutableAcl) acl.getParentAcl());
        }

        cache.put(new Element(acl.getObjectIdentity(), acl));
        cache.put(new Element(acl.getId(), acl));
    }

    private MutableAcl initializeTransientFields(MutableAcl value) {

        if (value.getParentAcl() != null) {
            initializeTransientFields((MutableAcl) value.getParentAcl());
        }
        return value;
    }

    public void clearCache() {
        cache.removeAll();
    }

@Required
    public void setCache(Ehcache cache) {
        this.cache = cache;
    }
}
