package com.melilla.gestPlanes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.NominasReclamadas;
import com.melilla.gestPlanes.repository.NominasReclamadasRepository;
import com.melilla.gestPlanes.service.NominaReclamadaService;

@Service
public class NominasReclamadasServiceImpl implements NominaReclamadaService {

	@Autowired
	NominasReclamadasRepository nominasReclamadasRepository;
	
	@Override
	public NominasReclamadas insertarNomina(NominasReclamadas nomina) {
		
		return nominasReclamadasRepository.save(nomina);
	}

	@Override
	public void eliminaNomina(long idNomina) {
		
		nominasReclamadasRepository.deleteById(idNomina);
		
	}

}
