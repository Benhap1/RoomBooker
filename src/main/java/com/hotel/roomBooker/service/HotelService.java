package com.hotel.roomBooker.service;

import com.hotel.roomBooker.DTO.HotelMapper;
import com.hotel.roomBooker.DTO.HotelRequestDTO;
import com.hotel.roomBooker.DTO.HotelResponseDTO;
import com.hotel.roomBooker.DTO.PaginatedHotelResponseDTO;
import com.hotel.roomBooker.exception.InvalidInputException;
import com.hotel.roomBooker.exception.ResourceNotFoundException;
import com.hotel.roomBooker.model.Hotel;
import com.hotel.roomBooker.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public PaginatedHotelResponseDTO getAllHotels(Pageable pageable) {
        Page<Hotel> hotelsPage = hotelRepository.findAll(pageable);
        List<HotelResponseDTO> hotelDTOs = hotelsPage.getContent().stream()
                .map(hotelMapper::toDTO)
                .toList();
        PaginatedHotelResponseDTO response = new PaginatedHotelResponseDTO();
        response.setHotels(hotelDTOs);
        response.setTotalRecords((int) hotelsPage.getTotalElements());
        return response;
    }

    public HotelResponseDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));
        return hotelMapper.toDTO(hotel);
    }


    public HotelResponseDTO createHotel(HotelRequestDTO hotelRequest) {
        Optional<Hotel> existingHotel = hotelRepository.findByNameAndCityAndAddress(
                hotelRequest.getName(),
                hotelRequest.getCity(),
                hotelRequest.getAddress()
        );
        if (existingHotel.isPresent()) {
            throw new InvalidInputException("Hotel with the same name, city, and address already exists");
        }
        Hotel hotel = hotelMapper.toEntity(hotelRequest);
        Hotel createdHotel = hotelRepository.save(hotel);
        return hotelMapper.toDTO(createdHotel);
    }


    public HotelResponseDTO updateHotel(Long id, HotelRequestDTO hotelRequest) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel with id " + id + " not found"));

        try {
            hotel.setName(hotelRequest.getName());
            hotel.setAnnouncementTitle(hotelRequest.getAnnouncementTitle());
            hotel.setCity(hotelRequest.getCity());
            hotel.setAddress(hotelRequest.getAddress());
            hotel.setDistanceFromCityCenter(hotelRequest.getDistanceFromCityCenter());
            Hotel updatedHotel = hotelRepository.save(hotel);
            return hotelMapper.toDTO(updatedHotel);
        } catch (Exception e) {
            throw new InvalidInputException("Invalid data provided for hotel update");
        }
    }

    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel with id " + id + " not found");
        }
        hotelRepository.deleteById(id);
    }
}