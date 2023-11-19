package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Salario;

public interface SalarioRepository extends JpaRepository<Salario, Integer>,RevisionRepository<Salario, Integer, Long>{
	List<Salario>findAllByPlanIdPlan(Long idPlan);
}
