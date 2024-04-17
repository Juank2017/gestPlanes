package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.TipoDocumentoPlan;
import com.melilla.gestPlanes.repository.TipoDocumentoPlanRepository;
import com.melilla.gestPlanes.service.TipoDocumentoPlanService;


@Service
public class TipoDocumentoPlanServiceImpl implements TipoDocumentoPlanService {
	
	@Autowired
	TipoDocumentoPlanRepository tipoDocumentoPlanRepository;

	@Override
	public List<TipoDocumentoPlan> obtenerTipoDocumentos() {
		
		return tipoDocumentoPlanRepository.findAll(Sort.by("tipo").ascending());
	}

	@Override
	public boolean existeTipoDocumento(String tipoDocumento) {
		
		return tipoDocumentoPlanRepository.findByTipo(tipoDocumento).isPresent();
	}

	@Override
	public TipoDocumentoPlan crearTipoDocumento(String tipoDocumento) {
		
		TipoDocumentoPlan tipo = new TipoDocumentoPlan();
		
		tipo.setTipo(tipoDocumento.toUpperCase());
		
		
		
		
		return tipoDocumentoPlanRepository.save(tipo);
	}

}
