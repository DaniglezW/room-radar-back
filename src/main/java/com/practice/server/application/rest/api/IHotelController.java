package com.practice.server.application.rest.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.response.HotelListResponse;
import com.practice.server.application.response.HotelResponse;
import com.practice.server.domain.model.Hotel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.practice.server.application.constants.Constants.*;

@RequestMapping(HOTEL_API_BASE)
public interface IHotelController {

    @GetMapping
    @Authenticated
    ResponseEntity<HotelListResponse> getAllHotels();

    @GetMapping("/{id}")
    ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<HotelResponse> createHotel(@RequestBody Hotel hotel);

    @PutMapping("/{id}")
    ResponseEntity<HotelResponse> updateHotel (@PathVariable Long id, @RequestBody Hotel hotel);

    @DeleteMapping("/{id}")
    ResponseEntity<HotelResponse> deleteHotelById(@PathVariable Long id);

}
