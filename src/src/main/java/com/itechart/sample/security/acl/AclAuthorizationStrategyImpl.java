package com.itechart.sample.security.acl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Strategy permits to call administrative methods on the <code>AclImpl</code>
 * to all authentificated users
 *
 * @author andrei.samarou
 */
public class AclAuthorizationStrategyImpl implements AclAuthorizationStrategy {

    @Override
    public void securityCheck(Acl acl, int changeType) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null
                || !securityContext.getAuthentication().isAuthenticated()) {
            throw new AccessDeniedException("Authenticated principal required to operate with ACLs");
        }
    }
}