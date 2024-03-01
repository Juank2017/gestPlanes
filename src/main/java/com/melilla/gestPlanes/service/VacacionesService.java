package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.AltaVacacionesDTO;
import com.melilla.gestPlanes.model.Vacaciones;

public interface VacacionesService {

	List<Vacaciones> listadoVacaciones(Long idPlan);
	
	List<Vacaciones> vacacionesTrabajador(Long idTrabajador);
	
	Vacaciones altaPeriodo(AltaVacacionesDTO periodo);
	
	void bajaPeriodo(Long idPeriodo);
	
	
}
