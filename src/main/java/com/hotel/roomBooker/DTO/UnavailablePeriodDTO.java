package com.hotel.roomBooker.DTO;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class UnavailablePeriodDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public UnavailablePeriodDTO(LocalDate checkInDate, LocalDate checkOutDate) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
