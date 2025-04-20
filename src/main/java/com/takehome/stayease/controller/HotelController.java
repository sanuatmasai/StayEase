package com.takehome.stayease.controller;

import java.util.List;
import com.takehome.stayease.dto.HotelDto;
import com.takehome.stayease.enums.Role;
import com.takehome.stayease.exception.UnauthorizedException;
import com.takehome.stayease.model.Hotel;
import com.takehome.stayease.model.User;
import com.takehome.stayease.service.HotelService;
import com.takehome.stayease.service.contextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private contextService tokenService;

    @PostMapping
    public Hotel createHotel(@RequestBody HotelDto hotelDTO) {
        User user = tokenService.getCurrentUser();
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only admins can create hotel");
        }
        return hotelService.createHotel(hotelDTO);
    }

    @PutMapping("/{hotelId}")
    public Hotel updateHotel(@PathVariable Long hotelId, @RequestBody HotelDto hotelDTO) {
        return hotelService.updateHotel(hotelId, hotelDTO);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long hotelId) {
        User user = tokenService.getCurrentUser();
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new UnauthorizedException("Only admins can delete hotel");
        }
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Hotel> getAllHotels() {
        return hotelService.getAllHotel();
    }
    
    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getAllHotels(@PathVariable Long hotelId) {
        return new ResponseEntity(hotelService.getHotel(hotelId), HttpStatus.OK);
    }
}