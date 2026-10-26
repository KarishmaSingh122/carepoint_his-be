package com.suma.carepoint.exceptions;


public class OneHcxAuthenticationException extends RuntimeException {

    public OneHcxAuthenticationException(String message) {
        super(message);
    }

    public OneHcxAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
