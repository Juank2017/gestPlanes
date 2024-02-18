package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;
import com.melilla.gestPlanes.repository.EquipoRepository;

import lombok.Data;

@Service
@Data
public class EquipoServiceImpl implements EquipoService{
	
	@Autowired
	private EquipoRepository equipoRepository;
	
	@Override
	public List<Equipo> equipos() {
		
		return equipoRepository.findAll();
	}

	@Override
	public Equipo crearEquipo(String nombre, Ciudadano jefeEquipo) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminaEquipo(Long idEquipo) {
		// TODO Auto-generated method stub
		
	}

}
