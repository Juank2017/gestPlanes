package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditarOcupacionDTO;
import com.melilla.gestPlanes.DTO.OcupacionResponseDTO;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Plan;

public interface OcupacionService {

	
	List<Ocupacion>obtenerOcupacionesPorCategoria(Long idCategoria);
	
	List<OcupacionResponseDTO>obtenerOcupacionesPlan(Plan plan);
	
	void borrarOcupacion(Long idOcupacion);
	
	Ocupacion crearOcupacion(CrearOcupacionDTO ocupacion);
	
	Ocupacion editarOcupacion(EditarOcupacionDTO ocupacion);
	
}
