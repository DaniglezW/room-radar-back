package com.practice.server.application.service.interfaces;

import com.practice.server.application.dto.request.ReservationRequest;
import com.practice.server.application.model.entity.Reservation;

import java.util.List;

public interface IReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id);

    Reservation update(Long id, Reservation reservation);

    void delete(Long id);

    List<Reservation> findByUser(Long userId);

    List<Reservation> findByRoom(Long roomId);

    List<Reservation> findByStatus(String status);

    Reservation createReservation(ReservationRequest request, String token);

}
