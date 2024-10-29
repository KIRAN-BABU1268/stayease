package com.crio.stayease.controller;

import com.crio.stayease.model.Hotel;
import com.crio.stayease.model.User;
import com.crio.stayease.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        Hotel createdHotel = hotelService.createHotel(hotel, admin);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HOTEL_MANAGER')")
    @PutMapping("/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long hotelId, @RequestBody Hotel updatedHotel, Authentication authentication) {
        User manager = (User) authentication.getPrincipal();
        Hotel hotel = hotelService.updateHotel(hotelId, updatedHotel, manager);
        return ResponseEntity.ok(hotel);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long hotelId, Authentication authentication) {
        User admin = (User) authentication.getPrincipal();
        hotelService.deleteHotel(hotelId, admin);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        List<Hotel> hotels = hotelService.findAllHotels();
        return ResponseEntity.ok(hotels);
    }
}
