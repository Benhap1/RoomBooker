package com.hotel.roomBooker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String userName;
    private String password;
    private String email;

    @ManyToOne
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

}
