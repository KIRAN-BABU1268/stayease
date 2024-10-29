package com.crio.stayease.service;

import com.crio.stayease.Role;
import com.crio.stayease.exception.*;
import com.crio.stayease.model.Booking;
import com.crio.stayease.model.Hotel;
import com.crio.stayease.model.User;
import com.crio.stayease.repo.BookingRepository;
import com.crio.stayease.repo.HotelRepository;
import com.crio.stayease.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking bookRoom(Long hotelId, Long userId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (hotel.getAvailableRooms() < 1) {
            throw new RoomUnavailableException("No rooms available.");
        }

        Booking booking = Booking.builder()
                .hotel(hotel)
                .user(user)
                .build();


        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        hotelRepository.save(hotel);

        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, User manager) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found."));

        if (!manager.getRole().equals(Role.Hotel_Manager)) {
            throw new UnauthorizedAccessException("Only hotel managers can cancel bookings.");
        }

        Hotel hotel = booking.getHotel();
        hotel.setAvailableRooms(hotel.getAvailableRooms() + 1);
        hotelRepository.save(hotel);

        bookingRepository.deleteById(bookingId);
    }

    public List<Booking> findBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}

