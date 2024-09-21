package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.RoomMapper;
import com.hotel.roomBooker.DTO.RoomRequestDTO;
import com.hotel.roomBooker.DTO.RoomResponseDTO;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.model.Hotel;
import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.repository.HotelRepository;
import com.hotel.roomBooker.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    public RoomResponseDTO getRoomById (Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room with id " + id + " not found"));
        return roomMapper.toDTO(room);
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
        room.setAvailableFrom(roomRequestDTO.getAvailableFrom());
        room.setAvailableTo(roomRequestDTO.getAvailableTo());

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
}


