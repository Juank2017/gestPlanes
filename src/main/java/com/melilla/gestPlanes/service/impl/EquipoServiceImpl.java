package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.exceptions.exceptions.EquipoNoEncontradoException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.EquipoRepository;
import com.melilla.gestPlanes.service.EquipoService;

import lombok.Data;
import lombok.extern.java.Log;

@Service
@Data
@Log
public class EquipoServiceImpl implements EquipoService{
	
	@Autowired
	private EquipoRepository equipoRepository;
	
	@Autowired
	private CiudadanoRepository ciudadanoRepository;
	
	@Override
	public List<Equipo> equipos(Long idPlan) {
		
		return equipoRepository.findAllByIdPlanIdPlan(idPlan);
	}

	@Override
	public Equipo crearEquipo(Equipo equipo) {
		
		return equipoRepository.save(equipo);
	}

	@Override
	public Equipo editarEquipo(Equipo equipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Equipo addComponente(Equipo equipo,Ciudadano ciudadano) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Equipo removeComponente(Equipo equipo,Ciudadano ciudadano) {
		
		log.warning("contiene "+equipo.getComponentes().contains(ciudadano));
		equipo.getComponentes().remove(ciudadano);
		log.warning("contiene "+equipo.getComponentes().contains(ciudadano));
		
		ciudadano.setEquipo(null);
		ciudadanoRepository.saveAndFlush(ciudadano);
		return equipoRepository.saveAndFlush(equipo);
	}

	@Override
	public void eliminaEquipo(Long idEquipo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Equipo equipo(Long idPlan,Long idEquipo) {
		
		return equipoRepository.findByIdPlanIdPlanAndIdEquipo(idPlan, idEquipo).orElseThrow(()->new EquipoNoEncontradoException(idEquipo));
	}

}
