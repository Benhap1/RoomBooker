package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.*;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.model.Hotel;
import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.UnavailableDate;
import com.hotel.roomBooker.repository.HotelRepository;
import com.hotel.roomBooker.repository.RoomRepository;
import com.hotel.roomBooker.repository.UnavailableDateRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final UnavailableDateRepository unavailableDateRepository;


    public RoomResponseDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        RoomResponseDTO roomDTO = roomMapper.toDTO(room);
        List<UnavailableDate> unavailableDates = unavailableDateRepository.findByRoomsContaining(room);
        List<LocalDate> unavailableDateList = unavailableDates.stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
        roomDTO.setUnavailableDates(unavailableDateList);
        return roomDTO;
    }

    @Transactional
    public RoomResponseDTO createRoom (RoomRequestDTO roomRequestDTO) {
        Hotel hotel = hotelRepository.findById(roomRequestDTO.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        Room room = roomMapper.toEntity(roomRequestDTO);
        room.setHotel(hotel);
        Room createdRoom = roomRepository.save(room);
        return roomMapper.toDTO(createdRoom);
    }

    @Transactional
    public RoomResponseDTO updateRoom(Long id, RoomRequestDTO roomRequestDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));

        room.setName(roomRequestDTO.getName());
        room.setDescription(roomRequestDTO.getDescription());
        room.setNumber(roomRequestDTO.getNumber());
        room.setPrice(roomRequestDTO.getPrice());
        room.setMaxPeople(roomRequestDTO.getMaxPeople());

        Hotel hotel = hotelRepository.findById(roomRequestDTO.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        room.setHotel(hotel);

        Room updatedRoom = roomRepository.save(room);
        return roomMapper.toDTO(updatedRoom);
    }

    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room with ID " + roomId + " not found"));
        roomRepository.delete(room);
    }

    public PaginatedRoomResponseDTO findRooms(RoomFilterDTO filter, Pageable pageable) {
        Specification<Room> specification = RoomSpecification.createSpecification(filter);
        Page<Room> roomsPage = roomRepository.findAll(specification, pageable);
        List<RoomResponseDTO> roomDTOs = new ArrayList<>();

        for (Room room : roomsPage.getContent()) {
            RoomResponseDTO roomDTO = roomMapper.toDTO(room);
            List<UnavailableDate> unavailableDates = unavailableDateRepository.findByRoomsContaining(room);
            List<LocalDate> unavailableDateList = unavailableDates.stream()
                    .map(UnavailableDate::getDate)
                    .collect(Collectors.toList());
            roomDTO.setUnavailableDates(unavailableDateList);
            roomDTOs.add(roomDTO);
        }
        PaginatedRoomResponseDTO response = new PaginatedRoomResponseDTO();
        response.setRooms(roomDTOs);
        response.setTotalRecords((int) roomsPage.getTotalElements());

        return response;
    }
}


