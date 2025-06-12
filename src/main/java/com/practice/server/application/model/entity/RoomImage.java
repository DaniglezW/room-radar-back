package com.practice.server.application.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bytea")
    private byte[] imageData;

    private String description;

    private Boolean isMain;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_id")
    private Room room;
}