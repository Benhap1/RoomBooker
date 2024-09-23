package com.hotel.roomBooker.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RoomResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String number;
    private long price;
    private int maxPeople;
    private Long hotelId;
    private List<LocalDate> unavailableDates;
}