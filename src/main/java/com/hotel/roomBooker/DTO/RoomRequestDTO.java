package com.hotel.roomBooker.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomRequestDTO {
    private String name;
    private String description;
    private String number;
    private long price;
    private int maxPeople;
    private Long hotelId;
}
