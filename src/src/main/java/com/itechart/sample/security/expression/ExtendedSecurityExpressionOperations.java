package com.itechart.sample.security.expression;

import org.springframework.security.access.expression.SecurityExpressionOperations;

/**
 * @author andrei.samarou
 */
public interface ExtendedSecurityExpressionOperations extends SecurityExpressionOperations {

    boolean hasPrivilege(String objectType, String action);

    boolean hasAnyPrivilege(String objectType, String... action);

}
