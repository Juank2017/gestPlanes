package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.Vacaciones;

public interface VacacionesRepository extends JpaRepository<Vacaciones, Long> {
	
	List<Vacaciones>findAllByIdPlanIdPlan(Long idPlan);
	
	List<Vacaciones>findAllByCiudadanoIdCiudadano(Long idCiudadano);

}
