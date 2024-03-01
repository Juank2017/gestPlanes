package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CreateEquipoDTO;
import com.melilla.gestPlanes.DTO.EditaEquipoDTO;
import com.melilla.gestPlanes.DTO.EquipoResponseDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;

public interface EquipoService {
	
	List<EquipoResponseDTO>equipos(Long idPlan);
	
	Equipo equipo(Long idPlan,Long idEquipo);
	
	Equipo crearEquipo(CreateEquipoDTO equipo);
	
	Equipo editarEquipo(EditaEquipoDTO equipo);
	
	Equipo addComponente(Equipo equipo,Ciudadano ciudadano);
	
	Equipo removeComponente(Equipo equipo,Ciudadano ciudadano);
	
	void eliminaEquipo(Long idEquipo);

}
