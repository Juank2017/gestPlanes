package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import com.melilla.gestPlanes.model.NotaCiudadano;

public interface NotasService {
	
	List<NotaCiudadano>notasTrabajador(Long idCiudadano);
	
	void borraNota(Long idNota);
	
	NotaCiudadano nota(Long idNota);

}
