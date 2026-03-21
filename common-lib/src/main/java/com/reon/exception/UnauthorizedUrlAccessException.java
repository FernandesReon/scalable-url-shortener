package com.reon.exception;

public class UnauthorizedUrlAccessException extends RuntimeException {
    public UnauthorizedUrlAccessException() {
        super("You do not have permission to modify this link.");
    }
}


