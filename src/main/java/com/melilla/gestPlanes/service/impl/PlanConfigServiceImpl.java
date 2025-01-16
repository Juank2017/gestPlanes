package com.melilla.gestPlanes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearConfigDTO;
import com.melilla.gestPlanes.DTO.updateConfigDTO;
import com.melilla.gestPlanes.exceptions.exceptions.PlanConfigNotFoundException;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.repository.PlanConfigRepository;
import com.melilla.gestPlanes.service.PlanConfigService;


@Service
public class PlanConfigServiceImpl implements PlanConfigService {
	
	
	@Autowired
	PlanConfigRepository planConfigRepository;

	@Override
	public PlanConfig crearConfig(CrearConfigDTO config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlanConfig actualizarConfig(updateConfigDTO config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarConfig(long idConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlanConfig obtenerConfig(long idPlan) {
		
		return planConfigRepository.findByPlanIdPlan(idPlan).orElseThrow(()->new PlanConfigNotFoundException(idPlan));
	}

}
