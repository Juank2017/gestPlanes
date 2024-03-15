package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.service.DashBoardService;


@Service
public class DashBoardServiceIMPL implements DashBoardService {

	@Autowired
	CiudadanoRepository ciudadanoRepository;
	
	
	@Override
	public List<Object> ciudadanosPorEstado(Long idPlan) {
		
		return ciudadanoRepository.findAllByIdPlanIdPlanGroupByEstado(idPlan);
	}

}
