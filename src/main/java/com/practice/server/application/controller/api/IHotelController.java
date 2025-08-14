package com.practice.server.application.controller.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.dto.HotelWithRoomsDTO;
import com.practice.server.application.dto.LocationSuggestionDto;
import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.request.HotelSearchRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;
import com.practice.server.application.dto.response.CountryAccommodationResponse;
import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.model.entity.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.practice.server.application.constants.Constants.*;

@RequestMapping(HOTEL_API_BASE)
public interface IHotelController {

    @GetMapping
    ResponseEntity<HotelListResponse> getAllHotels();

    @PostMapping
    @Authenticated
    ResponseEntity<HotelResponse> createHotel(@RequestBody Hotel hotel);

    @Authenticated
    @PutMapping("/{id}")
    ResponseEntity<HotelResponse> updateHotel (@PathVariable Long id, @RequestBody Hotel hotel);

    @Authenticated
    @DeleteMapping("/{id}")
    ResponseEntity<HotelResponse> deleteHotelById(@PathVariable Long id);

    @PostMapping("/{hotelId}/availability")
    ResponseEntity<List<AvailableRoomResponse>> checkAvailability(
            @PathVariable Long hotelId,
            @RequestBody AvailabilityRequest request);

    @GetMapping("/search")
    ResponseEntity<List<LocationSuggestionDto>> searchLocations(@RequestParam String query);

    @PostMapping("/search")
    List<HotelWithRoomsDTO> searchHotels(@RequestBody HotelSearchRequest request);

    @GetMapping("/top-rated")
    ResponseEntity<HotelListResponse> getTopRatedHotels(@RequestParam(defaultValue = "5") int limit);

    @GetMapping("/most-favorited")
    ResponseEntity<HotelListResponse> getMostFavoritedHotels(@RequestParam(defaultValue = "10") int limit);

    @GetMapping("/latest")
    ResponseEntity<HotelListResponse> getLatestHotels(@RequestParam(defaultValue = "10") int limit);

    @GetMapping("/popular-destinations")
    ResponseEntity<List<String>> getPopularDestinations(@RequestParam(defaultValue = "5") int limit);

    @Authenticated
    @GetMapping("/recommended")
    ResponseEntity<HotelListResponse> getRecommendedHotels(@RequestParam Long userId);

    @GetMapping("/luxury")
    ResponseEntity<HotelListResponse> getLuxuryHotels(
            @RequestParam String city,
            @RequestParam(defaultValue = "4") int minStars
    );

    @GetMapping("/{id}")
    ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id);

    @PostMapping("/accommodations-by-country")
    List<CountryAccommodationResponse> getAccommodationCount(@RequestBody List<String> countries);

}
