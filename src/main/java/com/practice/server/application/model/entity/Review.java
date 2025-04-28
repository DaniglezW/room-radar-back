package com.practice.server.application.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating; // 1 a 5
    private String comment;
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private Reservation reservation;

}