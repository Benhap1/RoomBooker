package com.hotel.roomBooker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotel.roomBooker.model.RoomBookingEvent;
import com.hotel.roomBooker.model.UserRegistrationEvent;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatisticService {

    private final MongoTemplate mongoTemplate;

    @KafkaListener(topics = "user-registration", groupId = "statistic-group")
    public void handleUserRegistrationEvent(String message) throws JsonProcessingException {
        UserRegistrationEvent event = new ObjectMapper().readValue(message, UserRegistrationEvent.class);
        mongoTemplate.save(event, "userRegistrations");
    }

    @KafkaListener(topics = "room-booking", groupId = "statistic-group")
    public void handleRoomBookingEvent(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        RoomBookingEvent event = objectMapper.readValue(message, RoomBookingEvent.class);
        mongoTemplate.save(event, "roomBookings");
    }
}
