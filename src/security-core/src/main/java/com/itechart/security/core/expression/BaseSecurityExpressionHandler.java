package com.itechart.security.core.expression;

import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.util.Assert;

/**
 * @author andrei.samarou
 */
public abstract class BaseSecurityExpressionHandler<T> extends AbstractSecurityExpressionHandler<T> {

    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        Assert.notNull(trustResolver, "trustResolver cannot be null");
        this.trustResolver = trustResolver;
    }

    protected AuthenticationTrustResolver getTrustResolver() {
        return trustResolver;
    }
}