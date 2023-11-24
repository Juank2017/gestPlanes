package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Organismo;

public interface OrganismoService {
	
	List<Organismo>obtenerOrganismosPorPlan(Long idPlan);

}
