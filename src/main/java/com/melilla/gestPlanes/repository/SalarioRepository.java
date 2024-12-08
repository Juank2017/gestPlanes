package com.melilla.gestPlanes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Salario;

public interface SalarioRepository extends JpaRepository<Salario, Long>,RevisionRepository<Salario, Long, Long>{
	List<Salario>findAllByPlanIdPlan(Long idPlan);
	
	Salario findByPlanIdPlan(long idPlan);
	
	Salario findByPlanIdPlanAndActivo(long idPlan,boolean activo);
	
	Optional<Salario> findByIdSalarioAndPlanIdPlan(long idSalario,long idPlan);
	
	
}
