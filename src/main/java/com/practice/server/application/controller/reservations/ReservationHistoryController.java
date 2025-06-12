package com.practice.server.application.controller.reservations;

import com.practice.server.application.annotations.Authenticated;
import com.practice.server.application.controller.api.IReservationHistoryController;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.dto.response.ReservationHistoryListResponse;
import com.practice.server.application.dto.response.ReservationHistoryResponse;
import com.practice.server.application.model.entity.ReservationHistory;
import com.practice.server.application.service.interfaces.IReservationHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Authenticated
@RestController
@RequiredArgsConstructor
public class ReservationHistoryController implements IReservationHistoryController {

    private final IReservationHistoryService historyService;

    @Override
    public ResponseEntity<ReservationHistoryListResponse> getAll() {
        return ResponseEntity.ok(
                new ReservationHistoryListResponse(0, "Historial de reservas", LocalDateTime.now(), historyService.findAll())
        );
    }

    @Override
    public ResponseEntity<ReservationHistoryResponse> getById(Long id) {
        return ResponseEntity.ok(
                new ReservationHistoryResponse(0, "Historial encontrado", LocalDateTime.now(), historyService.findById(id))
        );
    }

    @Override
    public ResponseEntity<ReservationHistoryResponse> create(ReservationHistory reservationHistory) {
        return ResponseEntity.ok(
                new ReservationHistoryResponse(0, "Historial creado", LocalDateTime.now(), historyService.save(reservationHistory))
        );
    }

    @Override
    public ResponseEntity<ReservationHistoryResponse> update(Long id, ReservationHistory reservationHistory) {
        return ResponseEntity.ok(
                new ReservationHistoryResponse(0, "Historial actualizado", LocalDateTime.now(), historyService.update(id, reservationHistory))
        );
    }

    @Override
    public ResponseEntity<PracticeResponse> delete(Long id) {
        historyService.delete(id);
        return ResponseEntity.ok(new PracticeResponse(0, "Historial eliminado"));
    }

    @Override
    public ResponseEntity<ReservationHistoryListResponse> getByUserId(Long userId) {
        return ResponseEntity.ok(
                new ReservationHistoryListResponse(0, "Historial del usuario", LocalDateTime.now(), historyService.findByUserId(userId))
        );
    }

}
