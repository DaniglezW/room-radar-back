package com.practice.server.application.model.entity;

import com.practice.server.application.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime paymentDate;
    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PAID, FAILED, PENDING...

    @ManyToOne(optional = false)
    private Reservation reservation;
}