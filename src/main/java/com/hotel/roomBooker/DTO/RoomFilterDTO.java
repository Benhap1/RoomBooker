package com.hotel.roomBooker.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomFilterDTO {
    private Long id;
    private String title;
    private Long minPrice;
    private Long maxPrice;
    private Integer maxPeople;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long hotelId;
}
