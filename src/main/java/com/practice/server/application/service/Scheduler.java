package com.practice.server.application.service;

import com.practice.server.application.model.entity.Reservation;
import com.practice.server.application.model.entity.ReservationHistory;
import com.practice.server.application.model.enums.ReservationAction;
import com.practice.server.application.model.enums.ReservationStatus;
import com.practice.server.application.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class Scheduler {

    private final ReservationRepository reservationRepository;
    private final ReservationHistoryService reservationHistoryService;

    public Scheduler(ReservationRepository reservationRepository, ReservationHistoryService reservationHistoryService) {
        this.reservationRepository = reservationRepository;
        this.reservationHistoryService = reservationHistoryService;
    }

    @Scheduled(cron = "0 0 0 * * ?") // todos los días a medianoche
    public void markPastReservationsCompleted() {
        List<Reservation> pastReservations = reservationRepository
                .findByStatusAndCheckOutDateBefore(ReservationStatus.CONFIRMED, LocalDate.now());

        for (Reservation reservation : pastReservations) {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);

            reservationHistoryService.save(
                    ReservationHistory.builder()
                            .reservation(reservation)
                            .user(reservation.getUser())
                            .action(ReservationAction.COMPLETED)
                            .actionDate(LocalDateTime.now())
                            .build()
            );
        }
    }

}
