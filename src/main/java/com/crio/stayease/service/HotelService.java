package com.crio.stayease.service;

import com.crio.stayease.Role;
import com.crio.stayease.exception.HotelNotFoundException;
import com.crio.stayease.exception.UnauthorizedAccessException;
import com.crio.stayease.model.Hotel;
import com.crio.stayease.model.User;
import com.crio.stayease.repo.HotelRepository;
import com.crio.stayease.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    public Hotel createHotel(Hotel hotel, User admin) {
        if (!admin.getRole().equals(Role.Admin)) {
            throw new UnauthorizedAccessException("Only admins can create hotels.");
        }
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long hotelId, Hotel updatedHotel, User manager) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException("Hotel not found."));

        if (!manager.getRole().equals(Role.Hotel_Manager)) {
            throw new UnauthorizedAccessException("Only hotel managers can update hotels.");
        }

        hotel.setDescription(updatedHotel.getDescription());
        hotel.setAvailableRooms(updatedHotel.getAvailableRooms());
        hotel.setLocation(updatedHotel.getLocation());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long hotelId, User admin) {
        if (!admin.getRole().equals(Role.Admin)) {
            throw new UnauthorizedAccessException("Only admins can delete hotels.");
        }
        hotelRepository.deleteById(hotelId);
    }

    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }
}

