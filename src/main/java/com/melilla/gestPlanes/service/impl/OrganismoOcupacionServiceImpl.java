package com.melilla.gestPlanes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreateOrganismoOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditOrganismoOcupacionDTO;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoOcupacionNotFoundException;
import com.melilla.gestPlanes.model.OrganismoOcupacion;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoOcupacionRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.service.OrganismoOcupacionService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class OrganismoOcupacionServiceImpl implements OrganismoOcupacionService {

	@Autowired
	OrganismoOcupacionRepository organismoOcupacionRepository;

	@Autowired
	OcupacionRepository ocupacionRepository;

	@Autowired
	OrganismoRepository organismoRepository;

	@Override
	public OrganismoOcupacion editOrganismoOcupacion(EditOrganismoOcupacionDTO organismoOcupacion) {

		OrganismoOcupacion orgOcu = organismoOcupacionRepository.findById(organismoOcupacion.getIdOrganismoOcupacion())
				.orElseThrow(() -> new OrganismoOcupacionNotFoundException());

		
		orgOcu.setNTrabajadores(Integer.parseInt(organismoOcupacion.getTrabajadores()));
		orgOcu.setOcupacion(ocupacionRepository.findById(organismoOcupacion.getIdOcupacion())
				.orElseThrow(() -> new OcupacionNotFoundException(organismoOcupacion.getIdOcupacion())));
		orgOcu.setOrganismo(organismoRepository.findById(organismoOcupacion.getIdOrganismo())
				.orElseThrow(() -> new OrganismoNotFoundException(organismoOcupacion.getIdOrganismo())));

		return organismoOcupacionRepository.save(orgOcu);
	}

	@Override
	public OrganismoOcupacion createOrganismoOcupacion(CreateOrganismoOcupacionDTO organismoOcupacion) {
		OrganismoOcupacion orgOcu = new OrganismoOcupacion();

		orgOcu.setNTrabajadores(organismoOcupacion.getNTrabajadores());
		orgOcu.setOcupacion(ocupacionRepository.findById(organismoOcupacion.getIdOcupacion())
				.orElseThrow(() -> new OcupacionNotFoundException(organismoOcupacion.getIdOcupacion())));
		orgOcu.setOrganismo(organismoRepository.findById(organismoOcupacion.getIdOrganismo())
				.orElseThrow(() -> new OrganismoNotFoundException(organismoOcupacion.getIdOrganismo())));

		return organismoOcupacionRepository.save(orgOcu);
	}

	@Override
	public void deleteOrganismoOcupacion(long idOrganismoOcupacion) {
		OrganismoOcupacion orgOcu = organismoOcupacionRepository.findById(idOrganismoOcupacion)
				.orElseThrow(() -> new OrganismoOcupacionNotFoundException());
		
		organismoOcupacionRepository.delete(orgOcu);

	}

}
