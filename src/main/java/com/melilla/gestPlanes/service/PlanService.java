package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import com.melilla.gestPlanes.model.Plan;

public interface PlanService {

	List<Plan>getPlanes();
	
	Optional<Plan>getPlan(Long idPlan);
	
	Plan seleccionarPlan(Long idPlan);
	
	Plan crearPlan(Plan plan);
	
	Plan getPlanActivo();
	
}
