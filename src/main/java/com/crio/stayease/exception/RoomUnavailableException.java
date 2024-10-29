package com.crio.stayease.exception;

public class RoomUnavailableException extends RuntimeException{
    public RoomUnavailableException(String msg){
        super(msg);
    }
}
