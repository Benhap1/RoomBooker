package com.hotel.roomBooker.DTO;

import com.hotel.roomBooker.model.Booking;
import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, UserMapper.class})
public interface BookingMapper {

    @Mapping(target = "roomNumbers", source = "rooms", qualifiedByName = "mapRoomsToRoomNumbers")
    @Mapping(target = "userName", source = "users", qualifiedByName = "mapFirstUserToUserName")
    BookingResponseDTO toResponseDTO(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rooms", ignore = true)
    @Mapping(target = "users", ignore = true)
    Booking toEntity(BookingRequestDTO bookingRequestDTO);

    @Named("mapRoomsToRoomNumbers")
    static List<String> mapRoomsToRoomNumbers(List<Room> rooms) {
        return rooms.stream()
                .map(Room::getNumber)
                .collect(Collectors.toList());
    }

    @Named("mapFirstUserToUserName")
    static String mapFirstUserToUsername(List<User> users) {
        if (users != null && !users.isEmpty()) {
            return users.get(0).getUserName();
        }
        return null;
    }
}
