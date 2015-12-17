package com.itechart.security.core.expression.web;

import com.itechart.security.core.expression.BaseSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

/**
 * Security expression handler for secured web requests
 *
 * @author andrei.samarou
 */
public class WebSecurityExpressionHandler extends BaseSecurityExpressionHandler<FilterInvocation> {

    @Override
    protected SecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, FilterInvocation filterInvocation) {
        WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(authentication, filterInvocation);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(getTrustResolver());
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}
