package com.practice.server.application.controller.reservations;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.controller.api.IReservationController;
import com.practice.server.application.dto.request.ReservationRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.dto.response.ReservationListResponse;
import com.practice.server.application.dto.response.ReservationResponse;
import com.practice.server.application.model.entity.Reservation;
import com.practice.server.application.service.interfaces.IReservationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Authenticated
@RestController
@RequiredArgsConstructor
public class ReservationController implements IReservationController {

    private final IReservationService reservationService;

    @Override
    public ResponseEntity<ReservationListResponse> getAllReservations() {
        return ResponseEntity.ok(
                new ReservationListResponse(0, "Listado de reservas", LocalDateTime.now(), reservationService.findAll())
        );
    }

    @Override
    public ResponseEntity<ReservationResponse> getReservationById(Long id) {
        Reservation res = reservationService.findById(id);
        return ResponseEntity.ok(
                new ReservationResponse(0, "Reserva encontrada", LocalDateTime.now(), res)
        );
    }

    @Override
    public ResponseEntity<ReservationResponse> updateReservation(Long id, Reservation reservation) {
        Reservation updated = reservationService.update(id, reservation);
        return ResponseEntity.ok(
                new ReservationResponse(0, "Reserva actualizada correctamente", LocalDateTime.now(), updated)
        );
    }

    @Override
    public ResponseEntity<PracticeResponse> deleteReservation(Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok(
                new PracticeResponse(0, "Reserva eliminada correctamente")
        );
    }

    @Override
    public ResponseEntity<ReservationListResponse> getReservationsByUser(Long userId) {
        return ResponseEntity.ok(
                new ReservationListResponse(0, "Reservas por usuario", LocalDateTime.now(), reservationService.findByUser(userId))
        );
    }

    @Override
    public ResponseEntity<ReservationListResponse> getReservationsByRoom(Long roomId) {
        return ResponseEntity.ok(
                new ReservationListResponse(0, "Reservas por habitación", LocalDateTime.now(), reservationService.findByRoom(roomId))
        );
    }

    @Override
    public ResponseEntity<ReservationListResponse> getReservationsByStatus(String status) {
        return ResponseEntity.ok(
                new ReservationListResponse(0, "Reservas por estado", LocalDateTime.now(), reservationService.findByStatus(status))
        );
    }

    @Override
    public ResponseEntity<ReservationResponse> createReservation(ReservationRequest request, String token,  HttpServletRequest httpServletRequest) {
        Reservation reservation = reservationService.createReservation(request, token, httpServletRequest);
        ReservationResponse response = mapToDto(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<ReservationListResponse> getMyReservations(String token, String status) {
        return ResponseEntity.ok(
                new ReservationListResponse(0, "Mis Reservas", LocalDateTime.now(), reservationService.getMyReservations(token,status))
        );
    }

    private ReservationResponse mapToDto(Reservation reservation) {
        return new ReservationResponse(
                201,
                "Reservation created successfully",
                LocalDateTime.now(),
                reservation
        );
    }

}
