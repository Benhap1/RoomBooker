package com.hotel.roomBooker.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RoomResponseDTO {
    private Long id;
    private String name;
    private String description;
    private int number;
    private long price;
    private int maxPeople;
    private LocalDate availableFrom;
    private LocalDate availableTo;
    private Long hotelId;
}