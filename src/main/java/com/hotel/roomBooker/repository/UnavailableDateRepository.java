package com.hotel.roomBooker.repository;

import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.UnavailableDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {
    List<UnavailableDate> findByRoomsContaining(Room room);
}
