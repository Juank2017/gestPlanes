package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	

}
