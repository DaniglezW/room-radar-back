package com.practice.server.application.controller.api;

import com.practice.server.application.dto.request.ReservationRequest;
import com.practice.server.application.dto.response.PracticeResponse;
import com.practice.server.application.dto.response.ReservationListAndImageResponse;
import com.practice.server.application.dto.response.ReservationListResponse;
import com.practice.server.application.dto.response.ReservationResponse;
import com.practice.server.application.model.entity.Reservation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.practice.server.application.constants.Constants.RESERVATION_API_BASE;

@RequestMapping(RESERVATION_API_BASE)
public interface IReservationController {

    @GetMapping
    ResponseEntity<ReservationListResponse> getAllReservations();

    @GetMapping("/{id}")
    ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation);

    @DeleteMapping("/{id}")
    ResponseEntity<PracticeResponse> deleteReservation(@PathVariable Long id);

    @GetMapping("/user/{userId}")
    ResponseEntity<ReservationListResponse> getReservationsByUser(@PathVariable Long userId);

    @GetMapping("/room/{roomId}")
    ResponseEntity<ReservationListResponse> getReservationsByRoom(@PathVariable Long roomId);

    @GetMapping("/status/{status}")
    ResponseEntity<ReservationListResponse> getReservationsByStatus(@PathVariable String status);

    @PostMapping
    ResponseEntity<ReservationResponse> createReservation(
            @RequestBody ReservationRequest request,
            @RequestHeader(value = "Authorization", required = false) String token,
            HttpServletRequest httpServletRequest
    );

    @GetMapping("/me")
    ResponseEntity<ReservationListAndImageResponse> getMyReservations(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String status
    );

}
