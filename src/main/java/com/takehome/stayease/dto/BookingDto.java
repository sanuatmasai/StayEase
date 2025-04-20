package com.takehome.stayease.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.takehome.stayease.model.Booking;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long hotelId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long bookingId;

    public static BookingDto mapToResponse(Booking b) {
        return BookingDto.builder()
                        .hotelId(b.getHotel().getId())
                        .checkInDate(b.getCheckInDate())
                        .checkOutDate(b.getCheckOutDate())
                        .bookingId(b.getId())
                        .build();
    }
}
