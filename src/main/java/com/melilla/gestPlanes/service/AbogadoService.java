package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearAbogadoDto;
import com.melilla.gestPlanes.model.Abogado;

public interface AbogadoService {
	
	List<Abogado> abogados();
	
	Abogado crearAbogado(CrearAbogadoDto abogado);
	
	void borrarAbogado(Long idAbogado);
	
	

}
