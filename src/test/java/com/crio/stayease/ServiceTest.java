package com.crio.stayease;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.crio.stayease.exception.HotelNotFoundException;
import com.crio.stayease.exception.RoomUnavailableException;
import com.crio.stayease.model.Booking;
import com.crio.stayease.model.Hotel;
import com.crio.stayease.model.User;
import com.crio.stayease.repo.BookingRepository;
import com.crio.stayease.repo.HotelRepository;
import com.crio.stayease.repo.UserRepository;
import com.crio.stayease.service.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

public class ServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private BookingService bookingService;

    public ServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBookRoom_Success() {
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("Dummy")
                .lastName("Dummy")
                .build();
        Hotel hotel = new Hotel(1L, "Hotel A", "NY", "A hotel", 10);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArgument(0));

        Booking booking = bookingService.bookRoom(1L, 1L);

        assertNotNull(booking);
        assertEquals(hotel.getId(), booking.getHotel().getId());
        assertEquals(user.getId(), booking.getUser().getId());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testBookRoom_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HotelNotFoundException.class, () -> bookingService.bookRoom(1L, 1L));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    public void testBookRoom_MaxBookingLimitExceeded() {
        User user =  User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("Dummy")
                .lastName("Dummy")
                .build();
        Hotel hotel = new Hotel(1L, "Hotel A", "NY", "A hotel", 0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(bookingRepository.findByUserId(1L)).thenReturn(List.of(new Booking(), new Booking()));
        assertThrows(RoomUnavailableException.class, () -> bookingService.bookRoom(1L, 1L));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

}

