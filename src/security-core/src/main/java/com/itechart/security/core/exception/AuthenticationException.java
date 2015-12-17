package com.itechart.security.core.exception;

/**
 * Throws if any problem occured while authentication or getting authenticated user information
 *
 * @author andrei.samarou
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
