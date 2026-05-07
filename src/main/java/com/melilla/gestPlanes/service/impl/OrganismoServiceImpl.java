package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearOrganismoDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.DTO.OrganismoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoConContratosException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanNotFoundException;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.repository.CategoriaRepository;
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
	public List<OrganismoDTO> obtenerOrganismosPorPlan(Long idPlan) {
		
		List<OrganismoDTO> salida = new ArrayList<OrganismoDTO>();
		
		List<Organismo> organismos = organismoRepository.findAllByIdPlanIdPlanOrderByNombreCortoOrganismoAsc(idPlan);
		
		Iterator<Organismo> it = organismos.iterator();
		
		while (it.hasNext()) {
			Organismo o = it.next();
			OrganismoDTO organismoDTO = new OrganismoDTO();
			organismoDTO.setNombreCortoOrganismo(o.getNombreCortoOrganismo());
			organismoDTO.setOrganismo(o.getOrganismo());
			organismoDTO.setDeleted(o.isDeleted());
			organismoDTO.setIdOrganismo(o.getIdOrganismo());
			
			salida.add(organismoDTO);
		
		}
		
		return salida;
	}


	@Override
	public Organismo crearOrganismo(CrearOrganismoDTO organismo) {
		
		Organismo nuevoOrganismo = new Organismo();
		
		nuevoOrganismo.setOrganismo(organismo.getOrganismo());
		nuevoOrganismo.setNombreCortoOrganismo(organismo.getNombreCortoOrganismo());
		nuevoOrganismo.setIdPlan(planService.getPlan(organismo.getIdPlan()));
		
		
		
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
		
		List<OrganismoDTO> organismos = obtenerOrganismosPorPlan(idPlan);
		
		List<Organismo> organismosCopiados = new ArrayList<Organismo>();
		
		Iterator<OrganismoDTO> it = organismos.iterator();
		
		while (it.hasNext()) {
			Organismo nuevoPlan =new Organismo();
			
			OrganismoDTO organismoAntiguoPlan = it.next();
			
			nuevoPlan.setOrganismo(organismoAntiguoPlan.getOrganismo());
			nuevoPlan.setNombreCortoOrganismo(organismoAntiguoPlan.getNombreCortoOrganismo());
			nuevoPlan.setIdPlan(planService.getPlanActivo());
			
			organismoRepository.save(nuevoPlan);
			
			organismosCopiados.add(nuevoPlan);
			
		}
		
		return organismosCopiados;
	}


	@Override
	public void borrarOrganismo(long idOrganismo) {

		Organismo org = organismoRepository.findById(idOrganismo).orElseThrow(()-> new OrganismoNotFoundException(idOrganismo));
		
		if (org.getContratos().isEmpty()) {
			organismoRepository.delete(org);
		}else {
			throw new OrganismoConContratosException(idOrganismo);
		}
	}


	@Override
	public Organismo save(Organismo organismo) {
		
		return organismoRepository.save(organismo);
	}

}
