package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.EstadoCiudadano;
import com.melilla.gestPlanes.repository.EstadoCiudadanoRepository;
import com.melilla.gestPlanes.service.EstadoCiudadanoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoCiudadanoServiceImpl implements EstadoCiudadanoService {

	@Autowired
	private EstadoCiudadanoRepository estadoCiudadanoRepository;

	@Override
	public List<EstadoCiudadano> obtenerEstados() {

		return estadoCiudadanoRepository.findAll();
	}

}
