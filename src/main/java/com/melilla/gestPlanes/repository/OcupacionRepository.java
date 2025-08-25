package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Plan;

public interface OcupacionRepository extends JpaRepository<Ocupacion, Long>, RevisionRepository<Ocupacion, Long, Long> {

	List<Ocupacion>findAllByCategoriaIdCategoriaOrderByOcupacionAsc(Long idCategoria);
	
	List<Ocupacion>findAllByIdPlan(Plan idPlan);
}
