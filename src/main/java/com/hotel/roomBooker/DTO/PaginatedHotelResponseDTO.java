package com.hotel.roomBooker.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedHotelResponseDTO {
    private List<HotelResponseDTO> hotels;
    private int totalRecords;
}

