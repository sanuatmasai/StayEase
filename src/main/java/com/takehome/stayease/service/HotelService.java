package com.takehome.stayease.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.takehome.stayease.dto.HotelDto;
import com.takehome.stayease.exception.BadRequestException;
import com.takehome.stayease.model.Hotel;
import com.takehome.stayease.repository.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @CacheEvict(value = "hotels", allEntries = true) // Invalidate full list
    public Hotel createHotel(HotelDto hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setDescription(hotelDTO.getDescription());
        hotel.setTotalRooms(hotelDTO.getTotalRooms());
        hotel.setAvailableRooms(hotelDTO.getAvailableRooms());

        return hotelRepository.save(hotel);
    }

    @Caching(evict = {
    		@CacheEvict(value = "hotels", allEntries = true), // Invalidate full list
    		@CacheEvict(value = "hotel", key = "#hotelId")    // Invalidate single hotel
    })
    public Hotel updateHotel(Long hotelId, HotelDto hotelDTO) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        hotel.setName(hotelDTO.getName());
        hotel.setAvailableRooms(hotelDTO.getAvailableRooms());

        return hotelRepository.save(hotel);
    }

    @Caching(evict = {
    		@CacheEvict(value = "hotels", allEntries = true), // Invalidate full list
    		@CacheEvict(value = "hotel", key = "#hotelId")    // Invalidate single hotel
    })
    public void deleteHotel(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }

    @Cacheable(value = "hotel", key = "#hotelId") // Cache by ID
    public Hotel getHotel(Long hotelId) {
        return hotelRepository.findById(hotelId)
                .orElseThrow(() -> new BadRequestException("Hotel not found"));
    }

    @Cacheable(value = "hotels")  // Cache full list
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }
}