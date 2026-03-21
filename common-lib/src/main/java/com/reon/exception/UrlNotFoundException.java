package com.reon.exception;

public class UrlNotFoundException extends RuntimeException{
    public UrlNotFoundException(String shortCode) {
        super(shortCode);
    }
}
