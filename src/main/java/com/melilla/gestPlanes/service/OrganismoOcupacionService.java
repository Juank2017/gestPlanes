package com.melilla.gestPlanes.service;

import com.melilla.gestPlanes.DTO.CreateOrganismoOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditOrganismoOcupacionDTO;
import com.melilla.gestPlanes.model.OrganismoOcupacion;

public interface OrganismoOcupacionService {
	
	OrganismoOcupacion editOrganismoOcupacion(EditOrganismoOcupacionDTO organismoOcupacion);
	
	OrganismoOcupacion createOrganismoOcupacion(CreateOrganismoOcupacionDTO organismoOcupacion);
	
	void deleteOrganismoOcupacion(long idOrganismoOcupacion);

}
