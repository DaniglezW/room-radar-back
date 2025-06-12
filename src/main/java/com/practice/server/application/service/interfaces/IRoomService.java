package com.practice.server.application.service.interfaces;

import com.practice.server.application.model.entity.Room;

import java.util.List;

public interface IRoomService {

    Room createRoom(Long hotelId, Room room);
    List<Room> getAllRooms();
    List<Room> getRoomsByHotel(Long hotelId);
    Room getRoomById(Long id);
    Room updateRoom(Long id, Room room);
    void deleteRoom(Long id);

}
