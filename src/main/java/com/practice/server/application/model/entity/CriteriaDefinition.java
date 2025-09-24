package com.practice.server.application.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "criteria_definitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;    // "Limpieza", "Servicio", "Ubicación"
    private Boolean active; // desactivar o activar criterios

}

