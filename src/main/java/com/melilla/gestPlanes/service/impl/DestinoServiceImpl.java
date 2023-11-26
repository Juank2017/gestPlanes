package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.service.DestinoService;

import lombok.Data;

@Service
@Data
public class DestinoServiceImpl implements DestinoService{
	
	@Autowired
	private DestinoRepository destinoRepository;
	
	@Autowired
	private OrganismoRepository organismoRepository;
	
	@Override
	public List<Destino> obtenerDestinosOrganismo(Long idOrganismo) {


		return destinoRepository.findAllByIdOrganismoIdOrganismoOrderByDestinoAsc(idOrganismo);
	}

	@Override
	public boolean existeDestino(String destino, Long idOrganismo) {
		
		return destinoRepository.findByDestinoAndIdOrganismoIdOrganismo(destino, idOrganismo).isPresent();
	}

	@Override
	public Destino crearDestino(Long idOrganismo, String destino) {
		
		Organismo organismo = organismoRepository.findById(idOrganismo).orElseThrow(()->new OrganismoNotFoundException(idOrganismo));
		
		Destino nuevoDestino= new Destino();
		nuevoDestino.setIdOrganismo(organismo);
		nuevoDestino.setDestino(destino.toUpperCase());
		
		
		
		return destinoRepository.save(nuevoDestino);
	}

}
