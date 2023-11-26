package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, RevisionRepository<Categoria, Long, Long>{

	List<Categoria>findAllByGrupoGrupoAndIdPlanIdPlanOrderByCategoriaAsc(Long grupo,Long idPlan);
}
