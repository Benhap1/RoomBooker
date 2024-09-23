package com.hotel.roomBooker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "unavailable_dates")
@NoArgsConstructor
@AllArgsConstructor
public class UnavailableDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToMany
    @JoinTable(
            name = "room_unavailable_dates",
            joinColumns = @JoinColumn(name = "unavailable_date_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )

    private List<Room> rooms;
}
