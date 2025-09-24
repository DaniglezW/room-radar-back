package com.practice.server.application.model.entity;

import com.practice.server.application.model.enums.ReservationAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime actionDate;

    @Enumerated(EnumType.STRING)
    private ReservationAction action; // Ej: "CREATED", "UPDATED", "CANCELLED"

    private String ipAddress;

    @Column(length = 1000)
    private String details;

    @ManyToOne
    private Reservation reservation;

    @ManyToOne
    private User user;
}

