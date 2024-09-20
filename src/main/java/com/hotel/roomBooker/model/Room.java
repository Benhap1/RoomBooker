package com.hotel.roomBooker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String number;
    private long price;
    private int maxPeople;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    @ManyToOne
    private Hotel hotel;

    @ManyToMany(mappedBy = "rooms")
    private List<Booking> bookings;

    @ManyToMany(mappedBy = "rooms")
    private List<UnavailableDate> unavailableDates;
}