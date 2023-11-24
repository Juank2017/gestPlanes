package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Ocupacion;

public interface OcupacionService {

	
	List<Ocupacion>obtenerOcupacionesPorCategoria(Long idCategoria);
	
}
