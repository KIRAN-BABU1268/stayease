package com.crio.stayease.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String msg){
        super(msg);
    }
}
