package com.itechart.sample.security.expression;

import org.springframework.security.access.expression.SecurityExpressionOperations;

/**
 * Extended interface for expression root objects used with expression-based security.
 * Contains methods for checking of privilege-based security constraints
 *
 * @author andrei.samarou
 */
public interface ExtendedSecurityExpressionOperations extends SecurityExpressionOperations {

    boolean hasPrivilege(String objectType, String action);

    boolean hasAnyPrivilege(String objectType, String... action);

}
