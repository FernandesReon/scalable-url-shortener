package com.reon.exception;

public class UrlInactiveException extends RuntimeException{
    public UrlInactiveException(String shortCode) {
        super(shortCode);
    }
}
