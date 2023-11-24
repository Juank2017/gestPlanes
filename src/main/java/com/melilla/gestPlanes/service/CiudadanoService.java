package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.model.Ciudadano;

public interface CiudadanoService {
	
	List<Ciudadano> getCiudadanos();
	
	Optional<Ciudadano> getCiudadano(Long idCiudadano);

	Ciudadano crearCiudadano(Ciudadano ciudadano);
	
	Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador);
}
