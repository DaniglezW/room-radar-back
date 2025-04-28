package com.practice.server.application.controller.room;

import com.practice.server.application.controller.api.IRoomController;
import com.practice.server.application.dto.response.RoomListResponse;
import com.practice.server.application.dto.response.RoomResponse;
import com.practice.server.application.model.entity.Room;
import com.practice.server.application.service.interfaces.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RoomController implements IRoomController {

    private final IRoomService iroomService;

    @Override
    public ResponseEntity<RoomListResponse> getAllRooms() {
        return ResponseEntity.ok(
                new RoomListResponse(0, "Habitaciones recuperadas", LocalDateTime.now(), iroomService.getAllRooms())
        );
    }

    @Override
    public ResponseEntity<RoomListResponse> getRoomsByHotel(Long hotelId) {
        return ResponseEntity.ok(
                new RoomListResponse(0, "Habitaciones recuperadas", LocalDateTime.now(), iroomService.getRoomsByHotel(hotelId))
        );
    }

    @Override
    public ResponseEntity<RoomResponse> getRoomById(Long roomId) {
        return ResponseEntity.ok(
                new RoomResponse(0, "Habitación encontrada", LocalDateTime.now(), iroomService.getRoomById(roomId))
        );
    }

    @Override
    public ResponseEntity<RoomResponse> createRoom(Long hotelId, Room room) {
        return ResponseEntity.ok(
                new RoomResponse(0, "Habitación creada", LocalDateTime.now(), iroomService.createRoom(hotelId, room))
        );
    }

    @Override
    public ResponseEntity<RoomResponse> updateRoom(Long roomId, Room room) {
        return ResponseEntity.ok(
                new RoomResponse(0, "Habitación actualizada", LocalDateTime.now(), iroomService.updateRoom(roomId, room))
        );
    }

    @Override
    public ResponseEntity<Void> deleteRoom(Long roomId) {
        iroomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
