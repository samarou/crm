package com.itechart.sample.security.acl;

import com.itechart.sample.model.security.ObjectIdentity;
import com.itechart.sample.service.AclService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * Allows permissions to be pre-cached when using pre or post filtering with expressions
 *
 * @author andrei.samarou
 */
public class AclPermissionCacheOptimizer implements PermissionCacheOptimizer {

    private AclService aclService;

    @Override
    @SuppressWarnings("unchecked")
    public void cachePermissionsFor(Authentication a, Collection<?> objects) {
        if (!CollectionUtils.isEmpty(objects)) {
            for (Object object : objects) {
                if (!(object instanceof ObjectIdentity)) {
                    throw new IllegalArgumentException("One of objects for security check is not " + ObjectIdentity.class);
                }
            }
            aclService.findAclsWithAncestors((List<ObjectIdentity>) objects);
        }
    }

    @Required
    public void setAclService(AclService aclService) {
        this.aclService = aclService;
    }
}
