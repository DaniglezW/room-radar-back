package com.practice.server.application.service.interfaces;

import com.practice.server.application.model.entity.ReservationHistory;

import java.util.List;

public interface IReservationHistoryService {

    List<ReservationHistory> findAll();

    ReservationHistory findById(Long id);

    ReservationHistory save(ReservationHistory reservationHistory);

    ReservationHistory update(Long id, ReservationHistory reservationHistory);

    void delete(Long id);

    List<ReservationHistory> findByUserId(Long userId);

}
