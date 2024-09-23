package com.hotel.roomBooker.controller;

import com.hotel.roomBooker.DTO.PaginatedRoomResponseDTO;
import com.hotel.roomBooker.DTO.RoomFilterDTO;
import com.hotel.roomBooker.DTO.RoomRequestDTO;
import com.hotel.roomBooker.DTO.RoomResponseDTO;
import com.hotel.roomBooker.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDTO> getRoomlById(@PathVariable Long id) {
        RoomResponseDTO roomResponse = roomService.getRoomById(id);
        return ResponseEntity.ok(roomResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody RoomRequestDTO roomRequest) {
        RoomResponseDTO createdRoom = roomService.createRoom(roomRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponseDTO> updateRoom(@PathVariable Long id, @RequestBody RoomRequestDTO roomRequest) {
        RoomResponseDTO updatedRoom = roomService.updateRoom(id, roomRequest);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<PaginatedRoomResponseDTO> getRooms(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) Integer maxPeople,
            @RequestParam(required = false) LocalDate checkInDate,
            @RequestParam(required = false) LocalDate checkOutDate,
            @RequestParam(required = false) Long hotelId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        RoomFilterDTO filter = new RoomFilterDTO();
        filter.setId(id);
        filter.setTitle(title);
        filter.setMinPrice(minPrice);
        filter.setMaxPrice(maxPrice);
        filter.setMaxPeople(maxPeople);
        filter.setCheckInDate(checkInDate);
        filter.setCheckOutDate(checkOutDate);
        filter.setHotelId(hotelId);
        Pageable pageable = PageRequest.of(page, size);
        PaginatedRoomResponseDTO rooms = roomService.findRooms(filter, pageable);
        return ResponseEntity.ok(rooms);
    }
}