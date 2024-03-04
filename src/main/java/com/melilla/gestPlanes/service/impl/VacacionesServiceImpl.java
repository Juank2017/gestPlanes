package com.melilla.gestPlanes.service.impl;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.AltaVacacionesDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Vacaciones;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.VacacionesRepository;
import com.melilla.gestPlanes.service.PlanService;
import com.melilla.gestPlanes.service.VacacionesService;

@Service
public class VacacionesServiceImpl implements VacacionesService {
	
	@Autowired
	VacacionesRepository vacacionesRepository;
	
	@Autowired
	CiudadanoRepository ciudadanoRepository;
	
	@Autowired
	PlanService planService;

	@Override
	public List<Vacaciones> listadoVacaciones(Long idPlan) {
		
		return vacacionesRepository.findAllByIdPlanIdPlan(idPlan);
	}

	@Override
	public List<Vacaciones> vacacionesTrabajador(Long idTrabajador) {
		
		return vacacionesRepository.findAllByCiudadanoIdCiudadano(idTrabajador);
	}

	@Override
	public Vacaciones altaPeriodo(AltaVacacionesDTO periodo) {
		
		Vacaciones nuevoPeriodo = new Vacaciones();
		
		Ciudadano trabajador = ciudadanoRepository.findById(periodo.getIdCiudadano()).orElseThrow(()->new CiudadanoNotFoundException(1l)); 
		
		nuevoPeriodo.setCiudadano(trabajador);
		nuevoPeriodo.setFechaInicio(periodo.getFechaInicio());
		nuevoPeriodo.setFechaFin(periodo.getFechaFinal());
		nuevoPeriodo.setIdPlan(planService.getPlanActivo());
		nuevoPeriodo.setDias((int)(ChronoUnit.DAYS.between( periodo.getFechaInicio(),periodo.getFechaFinal())+1));
		
		
		
		
		return vacacionesRepository.saveAndFlush(nuevoPeriodo);
	}

	@Override
	public void bajaPeriodo(Long idPeriodo) {
		
		vacacionesRepository.deleteById(idPeriodo);

	}

}
