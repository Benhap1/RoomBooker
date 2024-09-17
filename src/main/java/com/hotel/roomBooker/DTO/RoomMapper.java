package com.hotel.roomBooker.DTO;

import com.hotel.roomBooker.model.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "hotelId", target = "hotel.id")
    Room toEntity(RoomRequestDTO roomRequestDTO);

    @Mapping(source = "hotel.id", target = "hotelId")
    RoomResponseDTO toDTO(Room room);
}