package com.melilla.gestPlanes.service.impl;

import java.util.List;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;

public interface EquipoService {
	
	List<Equipo>equipos();
	
	Equipo crearEquipo(String nombre,Ciudadano jefeEquipo);
	
	Equipo editarEquipo(Equipo equipo);
	
	Equipo addComponente(Equipo equipo,Ciudadano ciudadano);
	
	Equipo removeComponente(Equipo equipo,Ciudadano ciudadano);
	
	void eliminaEquipo(Long idEquipo);

}
