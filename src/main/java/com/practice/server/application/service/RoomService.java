package com.practice.server.application.service;

import com.practice.server.application.dto.RoomDto;
import com.practice.server.application.exception.ResourceNotFoundException;
import com.practice.server.application.model.entity.Hotel;
import com.practice.server.application.model.entity.Room;
import com.practice.server.application.repository.HotelRepository;
import com.practice.server.application.repository.RoomRepository;
import com.practice.server.application.service.interfaces.IRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public Room createRoom(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getRoomsByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
    }

    @Override
    public Room updateRoom(Long id, Room updatedRoom) {
        Room room = getRoomById(id);
        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setType(updatedRoom.getType());
        room.setPricePerNight(updatedRoom.getPricePerNight());
        room.setAvailable(updatedRoom.getAvailable());
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
    }

    @Override
    public List<RoomDto> getRoomsByHotelWithOptionalAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = roomRepository.findAvailableRoomsByHotelAndDateRange(hotelId, checkIn, checkOut);
        return availableRooms.stream()
                .map(room -> new RoomDto(
                        room.getId(),
                        room.getRoomNumber(),
                        room.getType(),
                        room.getPricePerNight(),
                        room.getAvailable(),
                        room.getMaxGuests(),
                        room.getImages() != null ? room.getImages() : List.of()
                ))
                .toList();
    }
}
