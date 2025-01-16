package com.melilla.gestPlanes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.config.PlanConfig;

public interface PlanConfigRepository extends JpaRepository<PlanConfig, Long>, RevisionRepository<PlanConfig, Long, Long> {

	
	Optional<PlanConfig> findByPlanIdPlan(long idPlan);
}
