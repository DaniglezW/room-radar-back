package com.practice.server.application.controller.hotel;

import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;
import com.practice.server.application.dto.response.HotelListResponse;
import com.practice.server.application.dto.response.HotelResponse;
import com.practice.server.application.controller.api.IHotelController;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.service.interfaces.IAvailabilityService;
import com.practice.server.application.service.interfaces.IHotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

}
