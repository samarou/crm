package com.itechart.security.core.expression;

import com.itechart.security.core.SecurityOperations;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.DenyAllPermissionEvaluator;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Contains appropriate methods for processing of allowed security expressions
 *
 * @author andrei.samarou
 */
public abstract class AbstractSecurityExpressionRoot extends SecurityOperations implements ExtendedSecurityExpressionOperations {

    private PermissionEvaluator permissionEvaluator = new DenyAllPermissionEvaluator();
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    // Allow "permitAll" and "denyAll" expressions
    public final boolean permitAll = true;
    public final boolean denyAll = false;

    public AbstractSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public final boolean permitAll() {
        return true;
    }

    @Override
    public final boolean denyAll() {
        return false;
    }

    @Override
    public final boolean isAnonymous() {
        return trustResolver.isAnonymous(getAuthentication());
    }

    @Override
    public final boolean isAuthenticated() {
        return !isAnonymous();
    }

    @Override
    public final boolean isRememberMe() {
        return trustResolver.isRememberMe(getAuthentication());
    }

    @Override
    public final boolean isFullyAuthenticated() {
        return !trustResolver.isAnonymous(getAuthentication()) && !trustResolver.isRememberMe(getAuthentication());
    }

    @Override
    public final boolean hasPermission(Object target, Object permission) {
        return permissionEvaluator.hasPermission(getAuthentication(), target, permission);
    }

    @Override
    public final boolean hasPermission(Object targetId, String targetType, Object permission) {
        return permissionEvaluator.hasPermission(getAuthentication(), (Serializable) targetId,
                targetType, permission);
    }

    @Override
    public final boolean hasAuthority(String authority) {
        throw new UnsupportedOperationException("Use hasRole or hasPrivilege instead of");
    }

    @Override
    public final boolean hasAnyAuthority(String... authorities) {
        throw new UnsupportedOperationException("Use hasAnyRole or hasAnyPrivilege instead of");
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        Assert.notNull(trustResolver, "trustResolver cannot be null");
        this.trustResolver = trustResolver;
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        Assert.notNull(trustResolver, "permissionEvaluator cannot be null");
        this.permissionEvaluator = permissionEvaluator;
    }

}