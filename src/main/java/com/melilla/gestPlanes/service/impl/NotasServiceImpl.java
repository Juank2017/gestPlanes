package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.NotaNotFoundException;
import com.melilla.gestPlanes.model.NotaCiudadano;
import com.melilla.gestPlanes.repository.NotasRepository;
import com.melilla.gestPlanes.service.NotasService;

@Service
public class NotasServiceImpl implements NotasService{
	
	@Autowired
	private NotasRepository notasRepository;

	@Override
	public List<NotaCiudadano> notasTrabajador(Long idCiudadano) {
		
		return notasRepository.findByCiudadanoIdCiudadano(idCiudadano);
	}

	@Override
	public void borraNota(Long idNota) {
		notasRepository.deleteById(idNota);
		
	}

	@Override
	public NotaCiudadano nota(Long idNota) {
		
		return notasRepository.findById(idNota).orElseThrow(()-> new NotaNotFoundException(idNota+""));
	}

}
