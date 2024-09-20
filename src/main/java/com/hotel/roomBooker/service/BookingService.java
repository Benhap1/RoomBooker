package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.BookingMapper;
import com.hotel.roomBooker.DTO.BookingRequestDTO;
import com.hotel.roomBooker.DTO.BookingResponseDTO;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.exception.RoomNotAvailableException;
import com.hotel.roomBooker.model.Booking;
import com.hotel.roomBooker.model.Room;
import com.hotel.roomBooker.model.UnavailableDate;
import com.hotel.roomBooker.model.User;
import com.hotel.roomBooker.repository.BookingRepository;
import com.hotel.roomBooker.repository.RoomRepository;
import com.hotel.roomBooker.repository.UnavailableDateRepository;
import com.hotel.roomBooker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UnavailableDateRepository unavailableDateRepository;
    private final BookingMapper bookingMapper;


    @Transactional
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        User user = userRepository.findByUserName(bookingRequestDTO.getUserName())
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + bookingRequestDTO.getUserName() + " not found"));
        List<Room> rooms = roomRepository.findAllById(bookingRequestDTO.getRoomIds());
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
        return bookingMapper.toResponseDTO(savedBooking);
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
            String message = createAvailabilityMessage(rooms.size(), checkInDate, checkOutDate);
            throw new RoomNotAvailableException(message);
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

    private String createAvailabilityMessage(int roomCount, LocalDate checkInDate, LocalDate checkOutDate) {
        String roomWord = roomCount == 1 ? "Room" : "Rooms";
        String dateWord = checkInDate.equals(checkOutDate) ? "date" : "dates";
        String datePart = checkInDate.equals(checkOutDate) ?
                "on " + checkInDate :
                "from " + checkInDate + " to " + checkOutDate;
        return roomCount == 1 ?
                String.format("%s is not available on %s", roomWord, datePart) :
                String.format("%s are not available on %s", roomWord, datePart);
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