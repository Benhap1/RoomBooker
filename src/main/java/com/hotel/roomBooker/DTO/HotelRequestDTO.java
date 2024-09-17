package com.hotel.roomBooker.DTO;

import lombok.Data;

@Data
public class HotelRequestDTO {
    private String name;
    private String announcementTitle;
    private String city;
    private String address;
    private double distanceFromCityCenter;
    // rating и numberOfRatings не включены
}
