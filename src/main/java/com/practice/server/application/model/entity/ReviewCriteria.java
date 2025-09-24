package com.practice.server.application.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_criteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rating; // 0 a 10

    @ManyToOne
    @JsonIgnore
    private Review review;

    @ManyToOne
    private CriteriaDefinition criteriaDefinition;

}