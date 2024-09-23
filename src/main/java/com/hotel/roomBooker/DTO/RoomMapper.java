package com.hotel.roomBooker.DTO;

import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.UnavailableDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "hotelId", target = "hotel.id")
    Room toEntity(RoomRequestDTO roomRequestDTO);

    @Mapping(source = "hotel.id", target = "hotelId")
    @Mapping(target = "unavailableDates", expression = "java(mapUnavailableDates(room.getUnavailableDates()))")
    RoomResponseDTO toDTO(Room room);

    default List<LocalDate> mapUnavailableDates(List<UnavailableDate> unavailableDates) {
        if (unavailableDates == null || unavailableDates.isEmpty()) {
            return Collections.emptyList();
        }
        return unavailableDates.stream()
                .map(UnavailableDate::getDate)
                .collect(Collectors.toList());
    }
}
