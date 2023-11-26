package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Destino;

public interface DestinoService {

	List<Destino>obtenerDestinosOrganismo(Long idOrganismo);
	
	boolean existeDestino(String destino,Long idOrganismo);
	
	Destino crearDestino(Long idOrganismo,String destino);
}
