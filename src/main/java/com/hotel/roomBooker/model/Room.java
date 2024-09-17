package com.hotel.roomBooker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


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
    private int number;
    private long price;
    private int maxPeople;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    @ManyToOne
    private Hotel hotel;
}