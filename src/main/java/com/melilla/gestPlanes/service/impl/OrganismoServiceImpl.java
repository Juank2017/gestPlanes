package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.service.OrganismoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganismoServiceImpl implements OrganismoService {

	@Autowired
	private OrganismoRepository organismoRepository;
	
	
	@Override
	public List<Organismo> obtenerOrganismosPorPlan(Long idPlan) {
		
		return organismoRepository.findAllByIdPlanIdPlanOrderByNombreCortoOrganismoAsc(idPlan);
	}

}
