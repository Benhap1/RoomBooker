package com.hotel.roomBooker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hotel.roomBooker.DTO.BookingMapper;
import com.hotel.roomBooker.DTO.BookingRequestDTO;
import com.hotel.roomBooker.DTO.BookingResponseDTO;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.exception.RoomBookingException;
import com.hotel.roomBooker.exception.RoomNotAvailableException;
import com.hotel.roomBooker.model.*;
import com.hotel.roomBooker.repository.BookingRepository;
import com.hotel.roomBooker.repository.RoomRepository;
import com.hotel.roomBooker.repository.UnavailableDateRepository;
import com.hotel.roomBooker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UnavailableDateRepository unavailableDateRepository;
    private final BookingMapper bookingMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;


    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        User user = userRepository.findByUserName(bookingRequestDTO.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + bookingRequestDTO.getUserName() + " not found"));

        List<Room> rooms = roomRepository.findAllById(bookingRequestDTO.getRoomIds());
        if (rooms.isEmpty() || rooms.size() != bookingRequestDTO.getRoomIds().size()) {
            throw new ResourceNotFoundException("No rooms found for the provided IDs: " + bookingRequestDTO.getRoomIds());
        }

        checkRoomsAvailability(rooms, bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate());

        Booking booking = new Booking();
        booking.setCheckInDate(bookingRequestDTO.getCheckInDate());
        booking.setCheckOutDate(bookingRequestDTO.getCheckOutDate());
        booking.setRooms(rooms);
        booking.setUsers(List.of(user));

        Booking savedBooking = bookingRepository.save(booking);

        user.setBooking(savedBooking);
        userRepository.save(user);

        addUnavailableDates(rooms, bookingRequestDTO.getCheckInDate(), bookingRequestDTO.getCheckOutDate());

        RoomBookingEvent event = new RoomBookingEvent(
                user.getId(),
                convertToDate(booking.getCheckInDate()),
                convertToDate(booking.getCheckOutDate())
        );

        sendEvent("room-booking", event);

        return bookingMapper.toResponseDTO(savedBooking);
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }


    private void sendEvent(String topic, RoomBookingEvent event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            String jsonString = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(topic, jsonString);
        } catch (JsonProcessingException e) {
            throw new RoomBookingException("Failed to serialize RoomBookingEvent for Kafka", e);
        } catch (Exception e) {
            throw new RoomBookingException("Failed to send RoomBookingEvent to Kafka due to an unexpected error.", e);
        }
    }

    private void checkRoomsAvailability(List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {
        List<LocalDate> unavailableDates = new ArrayList<>();

        for (Room room : rooms) {
            List<UnavailableDate> roomUnavailableDates = unavailableDateRepository.findByRoomsContaining(room);
            for (UnavailableDate unavailableDate : roomUnavailableDates) {
                LocalDate date = unavailableDate.getDate();
                if (!date.isBefore(checkInDate) && !date.isAfter(checkOutDate)) {
                    unavailableDates.add(date);
                }
            }
        }

        if (!unavailableDates.isEmpty()) {
            throw new RoomNotAvailableException(createAvailabilityMessage(rooms.size(), checkInDate, checkOutDate, unavailableDates));
        }
    }

    private void addUnavailableDates(List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {
        LocalDate currentDate = checkInDate;
        while (!currentDate.isAfter(checkOutDate)) {
            UnavailableDate unavailableDate = new UnavailableDate();
            unavailableDate.setDate(currentDate);
            unavailableDate.setRooms(rooms);
            unavailableDateRepository.save(unavailableDate);
            currentDate = currentDate.plusDays(1);
        }
    }


    private String createAvailabilityMessage(int roomCount, LocalDate checkInDate, LocalDate checkOutDate, List<LocalDate> unavailableDates) {
        String roomWord = roomCount == 1 ? "Room" : "Rooms";
        String unavailableDatesString = unavailableDates.stream()
                .map(LocalDate::toString)
                .collect(Collectors.joining(", "));

        return String.format("%s %s not available on the following dates: %s", roomWord, roomCount == 1 ? "is" : "are", unavailableDatesString);
    }

    @Transactional
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();

        for (Booking booking : bookings) {
            bookingResponseDTOs.add(bookingMapper.toResponseDTO(booking));
        }
        return bookingResponseDTOs;
    }
}