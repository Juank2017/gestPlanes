package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.TipoDocumento;
import com.melilla.gestPlanes.repository.TipoDocumentoRepository;
import com.melilla.gestPlanes.service.TipoDocumentoService;


@Service
public class TipoDocumentoServiceImpl implements TipoDocumentoService {
	
	@Autowired
	TipoDocumentoRepository tipoDocumentoRepository;

	@Override
	public List<TipoDocumento> obtenerTipoDocumentos() {
		
		return tipoDocumentoRepository.findAll(Sort.by("tipo").ascending());
	}

	@Override
	public boolean existeTipoDocumento(String tipoDocumento) {
		
		return tipoDocumentoRepository.findByTipo(tipoDocumento).isPresent();
	}

	@Override
	public TipoDocumento crearTipoDocumento(String tipoDocumento) {
		
		TipoDocumento tipo = new TipoDocumento();
		
		tipo.setTipo(tipoDocumento.toUpperCase());
		
		
		
		
		return tipoDocumentoRepository.save(tipo);
	}

}
