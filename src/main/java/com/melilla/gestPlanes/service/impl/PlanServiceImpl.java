package com.melilla.gestPlanes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
	
	@Autowired
	PlanRepository planRepository;

	@Override
	public List<Plan> getPlanes() {
		
		return planRepository.findAll();
	}

	@Override
	public Optional<Plan> getPlan(Long idPlan) {
		return planRepository.findById(idPlan);
	}

	@Override
	public Plan seleccionarPlan(Long idPlan) {
		List<Plan> planes = getPlanes();
		
		planes.forEach((plan)->plan.setActivo(false));
		
		 Plan planSeleccionado = planRepository.findById(idPlan).orElseThrow(()->new PlanNotFoundException("No se encuentra el plan con id: "+idPlan));
		
		 planSeleccionado.setActivo(true);
		 
		 return planRepository.save(planSeleccionado);
		
	}

	public Plan crearPlan(Plan plan) {
		
		
		List<Plan> planes = getPlanes();
		
		planes.forEach((p)->p.setActivo(false));
		
		
		
		return planRepository.save(plan);
	}

}
