package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.Organismo;

public interface OrganismoRepository extends JpaRepository<Organismo, Long> {
	
	List<Organismo>findAllByIdPlanIdPlan(Long idPlan);

}
