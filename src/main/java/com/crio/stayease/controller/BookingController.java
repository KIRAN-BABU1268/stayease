package com.crio.stayease.controller;

import com.crio.stayease.model.Booking;
import com.crio.stayease.model.User;
import com.crio.stayease.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/hotels/{hotelId}/book")
    public ResponseEntity<Booking> bookRoom(@PathVariable Long hotelId, Authentication authentication) {
        User customer = (User) authentication.getPrincipal();
        Booking booking = bookingService.bookRoom(hotelId, customer.getId());
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId, Authentication authentication) {
        User manager = (User) authentication.getPrincipal();
        bookingService.cancelBooking(bookingId, manager);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Authentication authentication) {
        User customer = (User) authentication.getPrincipal();
        List<Booking> bookings = bookingService.findBookingsByUser(customer.getId());
        return ResponseEntity.ok(bookings);
    }
}

