package com.hotel.roomBooker.repository;

import com.hotel.roomBooker.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByNameAndCityAndAddress(String name, String city, String address);

    Page<Hotel> findAll(Specification<Hotel> specification, Pageable pageable);

}