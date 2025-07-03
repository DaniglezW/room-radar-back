package com.practice.server.application.controller.api;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.dto.RoomDto;
import com.practice.server.application.dto.response.RoomListResponse;
import com.practice.server.application.dto.response.RoomResponse;
import com.practice.server.application.model.entity.Room;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.practice.server.application.constants.Constants.ROOM_API_BASE;

@RequestMapping(ROOM_API_BASE)
public interface IRoomController {

    @GetMapping
    ResponseEntity<RoomListResponse> getAllRooms();

    @GetMapping("hotel/{hotelId}")
    ResponseEntity<RoomListResponse> getRoomsByHotel(@PathVariable Long hotelId);

    @GetMapping("/{roomId}")
    ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId);

    @Authenticated
    @PostMapping("/hotel/{hotelId}")
    ResponseEntity<RoomResponse> createRoom(@PathVariable Long hotelId, @RequestBody Room room);

    @Authenticated
    @PutMapping("/{roomId}")
    ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody Room room);

    @Authenticated
    @DeleteMapping("/{roomId}")
    ResponseEntity<Void> deleteRoom(@PathVariable Long roomId);

    @GetMapping("/by-hotel")
    ResponseEntity<List<RoomDto>> getRoomsByHotelWithOptionalAvailability(
            @RequestParam Long hotelId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    );


}
