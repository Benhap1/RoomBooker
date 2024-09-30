package com.hotel.roomBooker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBookingEvent {
    private Long userId;

    private Date checkInDate;
    private Date checkOutDate;

    public LocalDate getCheckInDate() {
        return checkInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}