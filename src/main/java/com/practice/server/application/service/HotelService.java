package com.practice.server.application.service;

import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.service.interfaces.IHotelService;
import com.practice.server.application.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class HotelService implements IHotelService {

    private static final String HOTEL_404_MSG = "Hotel not found for this id :: ";

    private final HotelRepository hotelRepository;

    @Autowired
    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelListResponse getAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        if (hotels.isEmpty()) {
            return new HotelListResponse(404, "No hotels found", LocalDateTime.now(), null);
        }
        return new HotelListResponse(200, "Hotels retrieved successfully", LocalDateTime.now(), hotels);
    }

    @Override
    public HotelResponse getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));
        return new HotelResponse(200, "Hotel found", LocalDateTime.now(), hotel);
    }

    @Override
    public HotelResponse createHotel(Hotel hotel) {
        hotel.setCreatedAt(LocalDateTime.now());
        Hotel createdHotel = hotelRepository.save(hotel);
        return new HotelResponse(201, "Hotel created successfully", LocalDateTime.now(), createdHotel);
    }

    @Override
    public HotelResponse updateHotel(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));

        hotel.setName(hotelDetails.getName());
        hotel.setDescription(hotelDetails.getDescription());
        hotel.setAddress(hotelDetails.getAddress());
        hotel.setCity(hotelDetails.getCity());
        hotel.setCountry(hotelDetails.getCountry());
        hotel.setStars(hotelDetails.getStars());
        hotel.setImages(hotelDetails.getImages());

        Hotel updatedHotel = hotelRepository.save(hotel);

        return new HotelResponse(200, "Hotel updated successfully", LocalDateTime.now(), updatedHotel);
    }

    @Override
    public HotelResponse deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(HOTEL_404_MSG + id));
        hotelRepository.delete(hotel);
        return new HotelResponse(200, "Hotel deleted successfully", LocalDateTime.now(), hotel);
    }

}
