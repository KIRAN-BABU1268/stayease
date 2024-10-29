package com.crio.stayease.exception;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String msg){
        super(msg);
    }
}
