package com.crio.stayease.exception;

import com.crio.stayease.model.Booking;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException(String msg){
        super(msg);
    }
}
