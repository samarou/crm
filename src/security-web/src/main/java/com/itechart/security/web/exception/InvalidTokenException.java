package com.itechart.security.web.exception;

/**
 * @author yauheni.putsykovich
 */
public class InvalidTokenException extends IllegalArgumentException {
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
