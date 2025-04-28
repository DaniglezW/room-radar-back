package com.practice.server.application.controller.api;

import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.dto.response.ReservationHistoryListResponse;
import com.practice.server.application.dto.response.ReservationHistoryResponse;
import com.practice.server.application.model.entity.ReservationHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.practice.server.application.constants.Constants.RESERVATION_HISTORY_API_BASE;

@RequestMapping(RESERVATION_HISTORY_API_BASE)
public interface IReservationHistoryController {

    @GetMapping
    ResponseEntity<ReservationHistoryListResponse> getAll();

    @GetMapping("/{id}")
    ResponseEntity<ReservationHistoryResponse> getById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ReservationHistoryResponse> create(@RequestBody ReservationHistory reservationHistory);

    @PutMapping("/{id}")
    ResponseEntity<ReservationHistoryResponse> update(@PathVariable Long id, @RequestBody ReservationHistory reservationHistory);

    @DeleteMapping("/{id}")
    ResponseEntity<PracticeResponse> delete(@PathVariable Long id);

    @GetMapping("/user/{userId}")
    ResponseEntity<ReservationHistoryListResponse> getByUserId(@PathVariable Long userId);

}
