package com.hotel.roomBooker.DTO;

import lombok.Data;
import java.util.List;

@Data
public class PaginatedRoomResponseDTO {
    private List<RoomResponseDTO> rooms;
    private int totalRecords;
}
