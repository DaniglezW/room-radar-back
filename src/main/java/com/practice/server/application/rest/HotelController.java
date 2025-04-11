package com.practice.server.application.rest;

import com.practice.server.application.response.HotelListResponse;
import com.practice.server.application.response.HotelResponse;
import com.practice.server.application.rest.api.IHotelController;
import com.practice.server.domain.model.Hotel;
import com.practice.server.domain.services.interfaces.IHotelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HotelController implements IHotelController {

    private final IHotelService hotelService;

    @Autowired
    public HotelController(IHotelService hotelService) {
        this.hotelService = hotelService;
    }

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

}
