package com.hotel.roomBooker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.roomBooker.DTO.UserMapper;
import com.hotel.roomBooker.DTO.UserRequestDTO;
import com.hotel.roomBooker.DTO.UserResponseDTO;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.exception.UserAlreadyExistsException;
import com.hotel.roomBooker.exception.UserRegistrationException;
import com.hotel.roomBooker.model.User;
import com.hotel.roomBooker.model.UserRegistrationEvent;
import com.hotel.roomBooker.model.UserRole;
import com.hotel.roomBooker.repository.UserRepository;
import com.hotel.roomBooker.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserResponseDTO getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User with name " + userName + " not found"));
        return userMapper.toDTO(user);
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUserNameOrEmail(userRequestDTO.getUserName(), userRequestDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with username and email already exists");
        }
        UserRole userRole = userRoleRepository.findByRoleName(userRequestDTO.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role with name " + userRequestDTO.getRoleName() + " not found"));
        User user = userMapper.toEntity(userRequestDTO);
        user.setUserRole(userRole);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        User savedUser = userRepository.save(user);

        UserRegistrationEvent event = new UserRegistrationEvent(savedUser.getId(), savedUser.getUserName());
        sendEvent("user-registration", event);

        return userMapper.toDTO(savedUser);
    }

    private void sendEvent(String topic, UserRegistrationEvent event) {
        try {
            kafkaTemplate.send(topic, new ObjectMapper().writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new UserRegistrationException("Failed to send UserRegistrationEvent to Kafka. Serialization error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserRegistrationException("Failed to send UserRegistrationEvent to Kafka due to an unexpected error.", e);
        }
    }

    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));
        existingUser.setUserName(userRequestDTO.getUserName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
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
