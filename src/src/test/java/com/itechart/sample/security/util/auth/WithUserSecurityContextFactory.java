package com.itechart.sample.security.util.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author andrei.samarou
 */
@Component
public class WithUserSecurityContextFactory implements WithSecurityContextFactory<WithUser> {

    public SecurityContext createSecurityContext(WithUser user) {
        String userName = user.value();
        Assert.hasLength(userName, "value() must be non empty");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userName, userName));
        return context;
    }
}