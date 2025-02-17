package com.melilla.gestPlanes.service;

import com.melilla.gestPlanes.DTO.CrearConfigDTO;
import com.melilla.gestPlanes.DTO.updateConfigDTO;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;

public interface PlanConfigService {
	
	PlanConfig crearConfig(Plan plan);
	
	PlanConfig actualizarConfig(updateConfigDTO config);
	
	void eliminarConfig(long idConfig);
	
	PlanConfig obtenerConfig(long idPlan);

}
