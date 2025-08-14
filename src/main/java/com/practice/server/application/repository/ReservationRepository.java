package com.practice.server.application.repository;

import com.practice.server.application.model.entity.Reservation;
import com.practice.server.application.model.entity.Room;
import com.practice.server.application.model.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByRoomAndStatus(Room room, ReservationStatus status);
    boolean existsByUserIdAndHotelIdAndCheckOutDateBefore(Long userId, Long hotelId, LocalDate date);

}
