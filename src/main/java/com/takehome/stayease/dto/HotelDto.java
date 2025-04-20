package com.takehome.stayease.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private String name;
    private String location;
    private String description;
    private int totalRooms;
    private int availableRooms;
}
