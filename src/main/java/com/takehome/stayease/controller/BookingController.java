package com.takehome.stayease.controller;

import com.takehome.stayease.dto.BookingDto;
import com.takehome.stayease.model.Booking;
import com.takehome.stayease.model.User;
import com.takehome.stayease.service.BookingService;
import com.takehome.stayease.service.contextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private contextService tokenService;

    @PostMapping("/{hotelId}")
    public BookingDto createBooking(@PathVariable Long hotelId, @RequestBody BookingDto bookingDTO) {
        User user = tokenService.getCurrentUser();
        return BookingDto.mapToResponse(bookingService.createBooking(hotelId, bookingDTO, user));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        User user = tokenService.getCurrentUser();
        bookingService.cancelBooking(bookingId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}")
    public BookingDto createBooking(@PathVariable Long bookingId) {
        return BookingDto.mapToResponse(bookingService.getBooking(bookingId));
    }
}