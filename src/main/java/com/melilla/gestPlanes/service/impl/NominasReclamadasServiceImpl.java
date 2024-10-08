package com.melilla.gestPlanes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.NominnasReclamadasNotFoundException;
import com.melilla.gestPlanes.mappers.nominasReclamadasMapper;
import com.melilla.gestPlanes.model.NominasReclamadas;
import com.melilla.gestPlanes.repository.NominasReclamadasRepository;
import com.melilla.gestPlanes.service.NominaReclamadaService;


@Service
public class NominasReclamadasServiceImpl implements NominaReclamadaService {

	@Autowired
	NominasReclamadasRepository nominasReclamadasRepository;
	
	@Autowired
	nominasReclamadasMapper nominasMapper;
	
	@Override
	public NominasReclamadas insertarNomina(NominasReclamadas nomina) {
		
		return nominasReclamadasRepository.save(nomina);
	}

	@Override
	public void eliminaNomina(long idNomina) {
		
		nominasReclamadasRepository.deleteById(idNomina);
		
	}

	@Override
	public NominasReclamadas editarNomina(NominasReclamadas nomina) {
		
	
		
		
		
		return nominasReclamadasRepository.saveAndFlush(nomina);
	}

	@Override
	public NominasReclamadas getNomina(long idNomina) {
		
		return nominasReclamadasRepository.findById(idNomina).orElseThrow(()-> new NominnasReclamadasNotFoundException(idNomina));
	}

}
