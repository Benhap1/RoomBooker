package com.hotel.roomBooker.DTO;

import lombok.Data;

@Data
public class HotelResponseDTO {
    private Long id;
    private String name;
    private String announcementTitle;
    private String city;
    private String address;
    private double distanceFromCityCenter;
    private int rating;
    private int numberOfRatings;
}
