package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, RevisionRepository<Categoria, Long, Long>{

	List<Categoria>findAllByGrupoAndIdPlanIdPlanOrderByCategoriaAsc(Long grupo,Long idPlan);
	
	//List<Categoria>findAllByIdPlanIdPlan(Long idPlan);
	
	@Query("SELECT c FROM Categoria c WHERE c.idPlan.idPlan = ?1 ")
	List<Categoria>findAllByIdPlanIdPlan(Long idPlan);
}
