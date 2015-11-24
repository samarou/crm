package com.itechart.sample.security.expression.method;

import com.itechart.sample.security.expression.BaseSecurityExpressionHandler;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.DefaultSecurityParameterNameDiscoverer;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Security expression handler for secured method invocations
 *
 * @author andrei.samarou
 */
public class MethodSecurityExpressionHandlerImpl extends BaseSecurityExpressionHandler<MethodInvocation>
        implements MethodSecurityExpressionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultSecurityParameterNameDiscoverer();
    private PermissionCacheOptimizer permissionCacheOptimizer;

    public StandardEvaluationContext createEvaluationContextInternal(Authentication auth, MethodInvocation mi) {
        return new MethodSecurityEvaluationContext(auth, mi, parameterNameDiscoverer);
    }

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        MethodSecurityExpressionRoot root = new MethodSecurityExpressionRoot(authentication);
        root.setThis(invocation.getThis());
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setRoleHierarchy(getRoleHierarchy());
        root.setTrustResolver(getTrustResolver());
        return root;
    }

    @SuppressWarnings("unchecked")
    public Object filter(Object filterTarget, Expression filterExpression, EvaluationContext ctx) {

        MethodSecurityExpressionOperations rootObject = (MethodSecurityExpressionOperations) ctx
                .getRootObject().getValue();

        final boolean debug = logger.isDebugEnabled();
        List retainList;

        if (debug) {
            logger.debug("Filtering with expression: " + filterExpression.getExpressionString());
        }

        if (filterTarget instanceof Collection) {
            Collection collection = (Collection) filterTarget;
            retainList = new ArrayList(collection.size());

            if (debug) {
                logger.debug("Filtering collection with " + collection.size() + " elements");
            }

            if (permissionCacheOptimizer != null) {
                permissionCacheOptimizer.cachePermissionsFor(
                        rootObject.getAuthentication(), collection);
            }

            for (Object filterObject : (Collection) filterTarget) {
                rootObject.setFilterObject(filterObject);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(filterObject);
                }
            }

            if (debug) {
                logger.debug("Retaining elements: " + retainList);
            }

            collection.clear();
            collection.addAll(retainList);

            return filterTarget;
        }

        if (filterTarget.getClass().isArray()) {
            Object[] array = (Object[]) filterTarget;
            retainList = new ArrayList(array.length);

            if (debug) {
                logger.debug("Filtering array with " + array.length + " elements");
            }

            if (permissionCacheOptimizer != null) {
                permissionCacheOptimizer.cachePermissionsFor(
                        rootObject.getAuthentication(), Arrays.asList(array));
            }

            for (Object o : array) {
                rootObject.setFilterObject(o);

                if (ExpressionUtils.evaluateAsBoolean(filterExpression, ctx)) {
                    retainList.add(o);
                }
            }

            if (debug) {
                logger.debug("Retaining elements: " + retainList);
            }

            Object[] filtered = (Object[]) Array.newInstance(
                    filterTarget.getClass().getComponentType(), retainList.size());

            for (int i = 0; i < retainList.size(); i++) {
                filtered[i] = retainList.get(i);
            }

            return filtered;
        }

        throw new IllegalArgumentException(
                "Filter target must be a collection or array type, but was "
                        + filterTarget);
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        Assert.notNull(parameterNameDiscoverer, "parameterNameDiscoverer cannot be null");
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public void setPermissionCacheOptimizer(PermissionCacheOptimizer permissionCacheOptimizer) {
        this.permissionCacheOptimizer = permissionCacheOptimizer;
    }

    public void setReturnObject(Object returnObject, EvaluationContext ctx) {
        ((MethodSecurityExpressionOperations) ctx.getRootObject().getValue()).setReturnObject(returnObject);
    }
}