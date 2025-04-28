package com.practice.server.application.controller.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;
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

    @GetMapping("/{id}")
    ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id);

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

}
