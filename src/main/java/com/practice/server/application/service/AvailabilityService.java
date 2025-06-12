package com.practice.server.application.service;

import com.practice.server.application.dto.request.AvailabilityRequest;
import com.practice.server.application.dto.response.AvailableRoomResponse;
import com.practice.server.application.model.entity.Reservation;
import com.practice.server.application.model.entity.Room;
import com.practice.server.application.model.enums.ReservationStatus;
import com.practice.server.application.repository.ReservationRepository;
import com.practice.server.application.repository.RoomRepository;
import com.practice.server.application.service.interfaces.IAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService implements IAvailabilityService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public List<AvailableRoomResponse> checkAvailability(Long hotelId, AvailabilityRequest request) {
        List<Room> hotelRooms = roomRepository.findByHotelId(hotelId);

        return hotelRooms.stream()
                .filter(room -> room.getMaxGuests() >= request.getGuests())
                .filter(room -> isAvailable(room, request.getCheckInDate(), request.getCheckOutDate()))
                .map(this::mapToDto)
                .toList();
    }

    boolean isAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        List<Reservation> reservations = reservationRepository.findByRoomAndStatus(room, ReservationStatus.CONFIRMED);

        return reservations.stream().noneMatch(r ->
                (checkIn.isBefore(r.getCheckOutDate()) && checkOut.isAfter(r.getCheckInDate()))
        );
    }

    private AvailableRoomResponse mapToDto(Room room) {
        return AvailableRoomResponse.builder()
                .roomId(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType().name())
                .pricePerNight(room.getPricePerNight())
                .available(room.getAvailable())
                .maxGuests(room.getMaxGuests())
                .build();
    }

}
