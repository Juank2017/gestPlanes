package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.ParteBaja;
import com.melilla.gestPlanes.model.Plan;

public interface ParteBajaRepository extends JpaRepository<ParteBaja, Long> {
	
	List<ParteBaja> findAllByCiudadanoIdCiudadano(long idCiudadano);
	
	List<ParteBaja> findAllByCiudadanoDNI(String DNI);
	
	List<ParteBaja> findAllByCiudadanoIdPlan(Plan plan);

}
