package com.hotel.roomBooker.DTO;

import lombok.Data;

@Data
public class HotelFilterDTO {
    private Long id;
    private String name;
    private String announcementTitle;
    private String city;
    private String address;
    private Double distanceFromCityCenter;
    private Double rating;
    private Integer numberOfRatings;
}
