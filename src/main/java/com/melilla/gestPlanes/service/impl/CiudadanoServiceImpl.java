package com.melilla.gestPlanes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.service.CiudadanoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CiudadanoServiceImpl implements CiudadanoService {
	
	@Autowired
	private CiudadanoRepository ciudadanoRepository;

	@Override
	public List<Ciudadano> getCiudadanos() {
		
		return ciudadanoRepository.findAll();
	}

	@Override
	public Optional<Ciudadano> getCiudadano(Long idCiudadano) {
		
		return ciudadanoRepository.findById(idCiudadano);
	}

	@Override
	public Ciudadano crearCiudadano(Ciudadano ciudadano) {
		
		return ciudadanoRepository.save(ciudadano);
	}

	@Override
	public Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador) {
		
		return null;
	}

	
	
	

}
