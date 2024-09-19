package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.UserMapper;
import com.hotel.roomBooker.DTO.UserRequestDTO;
import com.hotel.roomBooker.DTO.UserResponseDTO;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.exception.UserAlreadyExistsException;
import com.hotel.roomBooker.model.User;
import com.hotel.roomBooker.model.UserRole;
import com.hotel.roomBooker.repository.UserRepository;
import com.hotel.roomBooker.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;


    public UserResponseDTO getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User with name " + userName + " not found"));
        return userMapper.toDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUserNameOrEmail(userRequestDTO.getUsername(), userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with username and email already exists");
        }
        UserRole userRole = userRoleRepository.findByRoleName(userRequestDTO.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + userRequestDTO.getRoleName() + " not found"));
        User user = userMapper.toEntity(userRequestDTO);
        user.setUserRole(userRole);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));
        existingUser.setUserName(userRequestDTO.getUsername());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(userRequestDTO.getPassword());
        UserRole userRole = userRoleRepository.findByRoleName(userRequestDTO.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + userRequestDTO.getRoleName() + " not found"));
        existingUser.setUserRole(userRole);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));
        userRepository.delete(user);
    }
}
