package com.itechart.sample.security.util.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    public SecurityContext createSecurityContext(WithUser user) {
        String userName = user.value();
        Assert.hasLength(userName, "value() must be non empty");
        Assert.notNull(authenticationManager, "authenticationManager is not defined");
        Authentication token = new UsernamePasswordAuthenticationToken(userName, userName);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        try {
            context.setAuthentication(authenticationManager.authenticate(token));
        } catch (AuthenticationException e) {
            // store unauthenticated token in security context
            context.setAuthentication(token);
        }
        return context;
    }
}