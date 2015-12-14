package com.itechart.sample.security;

/**
 * @author andrei.samarou
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
