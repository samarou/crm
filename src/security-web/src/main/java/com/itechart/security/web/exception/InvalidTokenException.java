package com.itechart.security.web.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author yauheni.putsykovich
 */
public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
