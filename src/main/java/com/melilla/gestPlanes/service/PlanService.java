package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.DTO.CreatePlanDTO;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;

public interface PlanService {

	List<Plan>getPlanes();
	
	Optional<Plan>getPlan(Long idPlan);
	
	Plan seleccionarPlan(Long idPlan);
	
	Plan crearPlan(CreatePlanDTO plan);
	
	Plan getPlanActivo();
	
	Plan asignarConfiguracion(PlanConfig config);
	
	String copiarPlan(Long idPlan, String nombreNuevoPlan);
	
	String copiarPlanImportando(Long idPlan, String nombreNuevoPlan, MultipartFile file);
	
	Plan actualizarPlan(Long idPlan,String denominacion);
	
	void borrarPlan(Long idPlan);
	
}
