package com.crio.stayease.exception;

import java.security.PublicKey;

public class HotelNotFoundException extends RuntimeException{
    public HotelNotFoundException(String msg){
        super(msg);
    }

}
