package com.melilla.gestPlanes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long>, RevisionRepository<Plan, Long, Long> {
	
Optional<Plan>findByActivo(boolean activo);
}
