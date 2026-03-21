package com.reon.exception;

public class AliasAlreadyTakenException extends RuntimeException{
    public AliasAlreadyTakenException(String alias){
        super(alias);
    }
}
