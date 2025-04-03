package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;

public interface PlantillaContratoRepository extends JpaRepository<PlantillaContratoConfig, Long>, RevisionRepository<PlantillaContratoConfig, Long, Long> {

	
	List<PlantillaContratoConfig> findAllByPlan(Plan plan);
	
	PlantillaContratoConfig findByPlanAndActiva(Plan plan, boolean activa);
}
