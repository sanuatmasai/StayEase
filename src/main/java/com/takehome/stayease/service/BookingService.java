package com.takehome.stayease.service;

import java.time.LocalDate;
import com.takehome.stayease.dto.BookingDto;
import com.takehome.stayease.enums.Role;
import com.takehome.stayease.exception.InvalidCredentialsException;
import com.takehome.stayease.exception.UnauthorizedException;
import com.takehome.stayease.model.Booking;
import com.takehome.stayease.model.Hotel;
import com.takehome.stayease.model.User;
import com.takehome.stayease.repository.BookingRepository;
import com.takehome.stayease.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    public Booking createBooking(Long hotelId, BookingDto bookingDTO, User user) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        if (hotel.getAvailableRooms() <= 0) {
            throw new InvalidCredentialsException("No rooms available");
        }

        if (bookingDTO.getCheckInDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Check-in date must be in the future");
        }

        if (bookingDTO.getCheckOutDate().isBefore(bookingDTO.getCheckInDate())) {
            throw new RuntimeException("Check-out date must be after check-in date");
        }

        Booking booking = new Booking();
        booking.setHotel(hotel);
        booking.setCustomer(user);
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());

        hotel.setAvailableRooms(hotel.getAvailableRooms() - 1);
        hotelRepository.save(hotel);

        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, User user) {
        if (!user.getRole().equals(Role.HOTEL_MANAGER)) {
            throw new UnauthorizedException("Only hotel manager can cancel bookings");
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!(booking.getHotel().getAvailableRooms() >= 0)) {
            throw new RuntimeException("Booking already canceled");
        }
        bookingRepository.deleteById(bookingId);
    }

    public Booking getBooking(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new InvalidCredentialsException("No booking available"));
    }
}
