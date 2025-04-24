package com.novely.novely.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String msg) {
        super(msg);
    }
}
