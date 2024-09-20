package com.hotel.roomBooker.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private List<Long> roomIds;
    private String userName;
}