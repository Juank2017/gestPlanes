package com.melilla.gestPlanes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearConfigDTO;
import com.melilla.gestPlanes.DTO.updateConfigDTO;
import com.melilla.gestPlanes.exceptions.exceptions.PlanConfigNotFoundException;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.repository.PlanConfigRepository;
import com.melilla.gestPlanes.service.PlanConfigService;

import lombok.extern.java.Log;


@Service
@Log
public class PlanConfigServiceImpl implements PlanConfigService {
	
	
	@Autowired
	PlanConfigRepository planConfigRepository;

	@Override
	public PlanConfig crearConfig(Plan plan) {
		PlanConfig config = new PlanConfig();
		config.setPlan(plan);
		return planConfigRepository.save(config);
	}

	@Override
	public PlanConfig actualizarConfig(updateConfigDTO config) {
		
		PlanConfig configBBDD = planConfigRepository.findById(config.getIdConfig()).orElseThrow(()->new PlanConfigNotFoundException(config.getIdConfig()));
		
		configBBDD.setTempDir(config.getTempDir());
		configBBDD.setTrashcanDir(config.getTrashcanDir());
		configBBDD.setUploadDir(config.getUploadDir());
		
		log.warning(configBBDD.getUploadDir());
		
		return planConfigRepository.saveAndFlush(configBBDD);
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
