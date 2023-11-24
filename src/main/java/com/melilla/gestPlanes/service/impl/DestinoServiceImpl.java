package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.service.DestinoService;

import lombok.Data;

@Service
@Data
public class DestinoServiceImpl implements DestinoService{
	
	@Autowired
	private DestinoRepository destinoRepository;
	
	@Override
	public List<Destino> obtenerDestinosOrganismo(Long idOrganismo) {


		return destinoRepository.findAllByIdOrganismoIdOrganismo(idOrganismo);
	}

}
