package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearOrganismoDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.service.OrganismoService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganismoServiceImpl implements OrganismoService {

	@Autowired
	private OrganismoRepository organismoRepository;
	
	@Autowired
	private PlanService planService;
	
	@Override
	public List<Organismo> obtenerOrganismosPorPlan(Long idPlan) {
		
		return organismoRepository.findAllByIdPlanIdPlanOrderByNombreCortoOrganismoAsc(idPlan);
	}


	@Override
	public Organismo crearOrganismo(CrearOrganismoDTO organismo) {
		
		Organismo nuevoOrganismo = new Organismo();
		
		nuevoOrganismo.setOrganismo(organismo.getOrganismo());
		nuevoOrganismo.setNombreCortoOrganismo(organismo.getNombreCortoOrganismo());
		nuevoOrganismo.setIdPlan(planService.getPlan(organismo.getIdPlan()).orElseThrow(()-> new PlanNotFoundException("Plan no encontrado")));
		
		
		
		return organismoRepository.save(nuevoOrganismo);
	}


	@Override
	public Organismo editarOrganismo(EditarOrganismoDTO organismo) {
		
		Organismo organismoBBDD = organismoRepository.findById(organismo.getIdOrganismo()).orElseThrow(()-> new OrganismoNotFoundException(organismo.getIdOrganismo()));
		
		organismoBBDD.setNombreCortoOrganismo(organismo.getNombreCortoOrganismo());
		organismoBBDD.setOrganismo(organismo.getOrganismo());
		
		
	
		
		return organismoRepository.save(organismoBBDD);
	}


	@Override
	public List<Organismo> copiarDeOtroPlan(long idPlan) {
		
		List<Organismo> organismos = obtenerOrganismosPorPlan(idPlan);
		
		List<Organismo> organismosCopiados = new ArrayList<Organismo>();
		
		Iterator<Organismo> it = organismos.iterator();
		
		while (it.hasNext()) {
			Organismo nuevoPlan =new Organismo();
			
			Organismo organismoAntiguoPlan = it.next();
			
			nuevoPlan.setOrganismo(organismoAntiguoPlan.getOrganismo());
			nuevoPlan.setNombreCortoOrganismo(organismoAntiguoPlan.getNombreCortoOrganismo());
			nuevoPlan.setIdPlan(planService.getPlanActivo());
			
			organismoRepository.save(nuevoPlan);
			
			organismosCopiados.add(nuevoPlan);
			
		}
		
		return organismosCopiados;
	}

}
