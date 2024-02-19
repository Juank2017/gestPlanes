package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.Equipo;

public interface EquipoRepository extends JpaRepository<Equipo, Long>{

	List<Equipo>findAllByIdPlanIdPlan(Long idPlan);
}
