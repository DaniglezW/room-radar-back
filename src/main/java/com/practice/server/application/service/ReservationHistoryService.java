package com.practice.server.application.service;

import com.practice.server.application.model.entity.ReservationHistory;
import com.practice.server.application.repository.ReservationHistoryRepository;
import com.practice.server.application.service.interfaces.IReservationHistoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationHistoryService implements IReservationHistoryService {

    private final ReservationHistoryRepository repository;

    @Override
    public List<ReservationHistory> findAll() {
        return repository.findAll();
    }

    @Override
    public ReservationHistory findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Historial no encontrado con ID: " + id));
    }

    @Override
    public ReservationHistory save(ReservationHistory reservationHistory) {
        return repository.save(reservationHistory);
    }

    @Override
    public ReservationHistory update(Long id, ReservationHistory updated) {
        ReservationHistory existing = findById(id);

        existing.setActionDate(updated.getActionDate());
        existing.setAction(updated.getAction());
        existing.setReservation(updated.getReservation());
        existing.setUser(updated.getUser());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ReservationHistory> findByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }

}
