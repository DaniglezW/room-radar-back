package com.practice.server.application.repository;

import com.practice.server.application.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotelId(Long hotelId);

    @Query("""
        SELECT r FROM Room r
        WHERE r.hotel.id = :hotelId
          AND r.available = true
          AND NOT EXISTS (
              SELECT 1 FROM Reservation res
              WHERE res.room = r
                AND res.status IN ('PENDING', 'CONFIRMED')
                AND NOT (res.checkOutDate <= :checkIn OR res.checkInDate >= :checkOut)
          )
        """)
    List<Room> findAvailableRoomsByHotelAndDateRange(
            @Param("hotelId") Long hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

}
