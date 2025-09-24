package com.practice.server.application.repository;

import com.practice.server.application.model.entity.CriteriaDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CriteriaDefinitionRepository extends JpaRepository<CriteriaDefinition, Long> {

    // Obtener criterios activos por hotel
    List<CriteriaDefinition> findByActiveTrue();

}
