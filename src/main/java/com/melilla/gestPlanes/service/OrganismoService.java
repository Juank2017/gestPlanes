package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearOrganismoDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.model.Organismo;

public interface OrganismoService {
	
	List<Organismo>obtenerOrganismosPorPlan(Long idPlan);
	
	Organismo crearOrganismo(CrearOrganismoDTO orgamismo);
	
	Organismo editarOrganismo(EditarOrganismoDTO organismo);
	
	List<Organismo>copiarDeOtroPlan(long idPlan);

}
