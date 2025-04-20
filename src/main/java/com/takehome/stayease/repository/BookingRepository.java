package com.takehome.stayease.repository;

import java.util.Optional;
import com.takehome.stayease.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdAndCustomer_Id(Long bookingId, Long customerId);
    // Optional<Booking> findByIdAndCustomerId(Long bookingId, Long customerId); same as above
}
