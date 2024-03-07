package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearNotaDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.NotaNotFoundException;
import com.melilla.gestPlanes.model.NotaCiudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.NotasRepository;
import com.melilla.gestPlanes.service.NotasService;

@Service
public class NotasServiceImpl implements NotasService{
	
	@Autowired
	private NotasRepository notasRepository;
	
	@Autowired
	private CiudadanoRepository ciudadanoRepository;

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

	@Override
	public NotaCiudadano editaNota(NotaCiudadano nota) {
		NotaCiudadano notaAEditar = notasRepository.findById(nota.getIdNota()).orElseThrow(()->new NotaNotFoundException(nota.getIdNota()+""));
		
		notaAEditar.setAsunto(nota.getAsunto());
		notaAEditar.setNota(nota.getNota());
		notaAEditar.setPinned(nota.isPinned());
		
		return notasRepository.saveAndFlush(notaAEditar);
	}

	@Override
	public NotaCiudadano crearNota(CrearNotaDTO nota) {
		
		NotaCiudadano notaNueva = new NotaCiudadano();
		
		notaNueva.setAsunto(nota.getAsunto());
		notaNueva.setNota(nota.getNota());
		notaNueva.setFechaNota(nota.getFechaNota());
		notaNueva.setPinned(nota.isPinned());
		notaNueva.setCiudadano(ciudadanoRepository.findById(nota.getIdCiudadano()).orElseThrow(()->new CiudadanoNotFoundException(nota.getIdCiudadano())));
		
		return notasRepository.saveAndFlush(notaNueva);
	}

}
