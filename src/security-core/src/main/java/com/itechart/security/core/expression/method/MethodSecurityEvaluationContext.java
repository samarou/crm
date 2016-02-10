package com.itechart.security.core.expression.method;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Method;

/**
 * Internal security-specific EvaluationContext implementation which lazily adds the
 * method parameter values as variables (with the corresponding parameter names) if and
 * when they are required.
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class MethodSecurityEvaluationContext extends StandardEvaluationContext {

    private static final Logger logger = LoggerFactory.getLogger(MethodSecurityEvaluationContext.class);

    private ParameterNameDiscoverer parameterNameDiscoverer;
    private final MethodInvocation mi;
    private boolean argumentsAdded;

    public MethodSecurityEvaluationContext(Authentication user, MethodInvocation mi,
                                           ParameterNameDiscoverer parameterNameDiscoverer) {
        this.mi = mi;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    @Override
    public Object lookupVariable(String name) {
        Object variable = super.lookupVariable(name);

        if (variable != null) {
            return variable;
        }

        if (!argumentsAdded) {
            addArgumentsAsVariables();
            argumentsAdded = true;
        }

        variable = super.lookupVariable(name);

        if (variable != null) {
            return variable;
        }

        return null;
    }

    public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    private void addArgumentsAsVariables() {
        Object[] args = mi.getArguments();

        if (args.length == 0) {
            return;
        }

        Object targetObject = mi.getThis();
        // SEC-1454
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(targetObject);

        if (targetClass == null) {
            targetClass = targetObject.getClass();
        }

        Method method = AopUtils.getMostSpecificMethod(mi.getMethod(), targetClass);
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);

        if (paramNames == null) {
            logger.warn("Unable to resolve method parameter names for method: {}. " +
                    "Debug symbol information is required if you are using parameter names in expressions.",
                    method);
            return;
        }

        for (int i = 0; i < args.length; i++) {
            super.setVariable(paramNames[i], args[i]);
        }
    }
}