package com.hotel.roomBooker.controller;

import com.hotel.roomBooker.DTO.HotelFilterDTO;
import com.hotel.roomBooker.DTO.HotelRequestDTO;
import com.hotel.roomBooker.DTO.HotelResponseDTO;
import com.hotel.roomBooker.DTO.PaginatedHotelResponseDTO;
import com.hotel.roomBooker.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<PaginatedHotelResponseDTO> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PaginatedHotelResponseDTO response = hotelService.getAllHotels(pageable);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HotelResponseDTO> getHotelById(@PathVariable Long id) {
        HotelResponseDTO hotelResponse = hotelService.getHotelById(id);
        return ResponseEntity.ok(hotelResponse);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HotelResponseDTO> createHotel(@RequestBody HotelRequestDTO hotelRequest) {
        HotelResponseDTO createdHotel = hotelService.createHotel(hotelRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HotelResponseDTO> updateHotel(@PathVariable Long id, @RequestBody HotelRequestDTO hotelRequest) {
        HotelResponseDTO updatedHotel = hotelService.updateHotel(id, hotelRequest);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/rating")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<HotelResponseDTO> updateHotelRating(@PathVariable Long id, @RequestParam int newMark) {
        HotelResponseDTO updatedHotel = hotelService.updateRating(id, newMark);
        return ResponseEntity.ok(updatedHotel);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<PaginatedHotelResponseDTO> getFilteredHotels(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String announcementTitle,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double distanceFromCityCenter,
            @RequestParam(required = false) Double rating,
            @RequestParam(required = false) Integer numberOfRatings,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        HotelFilterDTO filter = new HotelFilterDTO();
        filter.setId(id);
        filter.setName(name);
        filter.setAnnouncementTitle(announcementTitle);
        filter.setCity(city);
        filter.setAddress(address);
        filter.setDistanceFromCityCenter(distanceFromCityCenter);
        filter.setRating(rating);
        filter.setNumberOfRatings(numberOfRatings);
        Pageable pageable = PageRequest.of(page, size);
        PaginatedHotelResponseDTO response = hotelService.getFilteredHotels(filter, pageable);

        return ResponseEntity.ok(response);
    }
}