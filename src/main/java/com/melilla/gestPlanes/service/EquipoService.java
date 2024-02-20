package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;

public interface EquipoService {
	
	List<Equipo>equipos(Long idPlan);
	
	Equipo equipo(Long idPlan,Long idEquipo);
	
	Equipo crearEquipo(Equipo equipo);
	
	Equipo editarEquipo(Equipo equipo);
	
	Equipo addComponente(Equipo equipo,Ciudadano ciudadano);
	
	Equipo removeComponente(Equipo equipo,Ciudadano ciudadano);
	
	void eliminaEquipo(Long idEquipo);

}
