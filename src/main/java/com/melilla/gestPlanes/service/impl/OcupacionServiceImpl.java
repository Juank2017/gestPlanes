package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.service.OcupacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OcupacionServiceImpl implements OcupacionService {
	
	@Autowired
	private OcupacionRepository ocupacionRepository;
	
	
	@Override
	public List<Ocupacion> obtenerOcupacionesPorCategoria(Long idCategoria) {
		
		return ocupacionRepository.findAllByCategoriaIdCategoria(idCategoria);
	}

}
