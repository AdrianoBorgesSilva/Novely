package com.novely.novely.exception;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String msg) {
        super(msg);
    }
}
