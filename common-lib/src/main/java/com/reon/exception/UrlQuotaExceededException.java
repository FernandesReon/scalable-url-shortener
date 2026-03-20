package com.reon.exception;

public class UrlQuotaExceededException extends RuntimeException{
    public UrlQuotaExceededException(String message) {
        super(message);
    }
}