package com.crio.stayease.exception;

public class UserAlreadyExistsException extends RuntimeException {
public  UserAlreadyExistsException(String msg){
    super(msg);
}
}
