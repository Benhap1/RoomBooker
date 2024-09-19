package com.hotel.roomBooker.DTO;

import com.hotel.roomBooker.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userRole", ignore = true)
    User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(source = "user.userRole.roleName", target = "roleName")
    @Mapping(source = "user.userName", target = "username")
    UserResponseDTO toDTO(User user);
}
