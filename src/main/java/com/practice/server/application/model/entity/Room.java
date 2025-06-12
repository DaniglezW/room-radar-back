package com.practice.server.application.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.practice.server.application.model.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType type; // SINGLE, DOUBLE, SUITE, FAMILY.

    private Double pricePerNight;

    private Boolean available;

    private Integer maxGuests;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> images = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

}
