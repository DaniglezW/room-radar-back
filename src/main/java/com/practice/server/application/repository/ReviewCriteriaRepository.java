package com.practice.server.application.repository;

import com.practice.server.application.model.entity.ReviewCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCriteriaRepository extends JpaRepository<ReviewCriteria, Long> {

    // Todos los criterios asociados a una review
    List<ReviewCriteria> findByReviewId(Long reviewId);

    // Si quieres sacar, por ejemplo, todos los ratings de "Limpieza" en un hotel
    List<ReviewCriteria> findByReviewHotelIdAndCriteriaDefinitionName(Long hotelId, String criteriaName);

    // Otra variante si trabajas por ID del criterio
    List<ReviewCriteria> findByReviewHotelIdAndCriteriaDefinitionId(Long hotelId, Long criteriaId);

    // Obtener todos los subratings de un hotel (por sus reviews)
    List<ReviewCriteria> findByReviewHotelId(Long hotelId);

}
