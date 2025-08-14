package com.practice.server.application.controller.hotel;

import com.practice.server.application.dto.HotelWithRoomsDTO;
import com.practice.server.application.dto.LocationSuggestionDto;
import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.request.HotelSearchRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;
import com.practice.server.application.dto.response.CountryAccommodationResponse;
import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.controller.api.IHotelController;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.service.interfaces.IAvailabilityService;
import com.practice.server.application.service.interfaces.IHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HotelController implements IHotelController {

    private final IHotelService hotelService;

    private final IAvailabilityService availabilityService;

    @Override
    public ResponseEntity<HotelListResponse> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @Override
    public ResponseEntity<HotelResponse> getHotelById(Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @Override
    public List<CountryAccommodationResponse> getAccommodationCount(List<String> countries) {
        return hotelService.getAccommodationCount(countries);
    }

    @Override
    public ResponseEntity<HotelResponse> createHotel(Hotel hotel) {
        return ResponseEntity.ok(hotelService.createHotel(hotel));
    }

    @Override
    public ResponseEntity<HotelResponse> updateHotel(Long id, Hotel hotel) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotel));
    }

    @Override
    public ResponseEntity<HotelResponse> deleteHotelById(Long id) {
        return ResponseEntity.ok(hotelService.deleteHotel(id));
    }

    @Override
    public ResponseEntity<List<AvailableRoomResponse>> checkAvailability(Long hotelId, AvailabilityRequest request) {
        return ResponseEntity.ok(availabilityService.checkAvailability(hotelId, request));
    }

    @Override
    public ResponseEntity<List<LocationSuggestionDto>> searchLocations(@RequestParam String query) {
        return ResponseEntity.ok(hotelService.search(query));
    }

    @Override
    public List<HotelWithRoomsDTO> searchHotels(HotelSearchRequest request) {
        return hotelService.searchHotels(request);
    }

    @Override
    public ResponseEntity<HotelListResponse> getTopRatedHotels(int limit) {
        return ResponseEntity.ok(hotelService.getTopRatedHotels(limit));
    }

    @Override
    public ResponseEntity<HotelListResponse> getMostFavoritedHotels(int limit) {
        return ResponseEntity.ok(hotelService.getMostFavoritedHotels(limit));
    }

    @Override
    public ResponseEntity<HotelListResponse> getLatestHotels(int limit) {
        return ResponseEntity.ok(hotelService.getLatestHotels(limit));
    }

    @Override
    public ResponseEntity<List<String>> getPopularDestinations(int limit) {
        return ResponseEntity.ok(hotelService.getPopularDestinations(limit));
    }

    @Override
    public ResponseEntity<HotelListResponse> getRecommendedHotels(Long userId) {
        return ResponseEntity.ok(hotelService.getRecommendedHotels(userId));
    }

    @Override
    public ResponseEntity<HotelListResponse> getLuxuryHotels(String city, int minStars) {
        return ResponseEntity.ok(hotelService.getLuxuryHotelsInCity(city, minStars));
    }

}
