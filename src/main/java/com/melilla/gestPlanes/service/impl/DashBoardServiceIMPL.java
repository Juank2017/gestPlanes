package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.DashBoardBajasDTO;
import com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.service.DashBoardService;

import lombok.extern.java.Log;


@Service
@Log
public class DashBoardServiceIMPL implements DashBoardService {

	@Autowired
	CiudadanoRepository ciudadanoRepository;
	
	
	@Override
	public List<DashBoardEstadoGeneroDTO> ciudadanosPorEstado(Long idPlan) {
		List<DashBoardEstadoGeneroDTO> resultado = ciudadanoRepository.findAllByIdPlanIdPlanGroupByEstado(idPlan);
		
		List<DashBoardEstadoGeneroDTO> salida = new ArrayList<>();
		
		return resultado ;
	}


	@Override
	public List<DashBoardBajasDTO> trabajadoresEnBaja(Long idPlan) {
		List<Ciudadano> bajasLaborales = ciudadanoRepository.findAllByidPlanIdPlanAndBajaLaboralTrue(idPlan);
		List<Ciudadano> bajasMaternidad= ciudadanoRepository.findAllByidPlanIdPlanAndBajaMaternalTrue(idPlan);
		List<DashBoardBajasDTO> resultado = new ArrayList<>();
		
		DashBoardBajasDTO bajas = new DashBoardBajasDTO();
		bajas.setBaja("laboral");
		bajas.setCantidad(bajasLaborales.size()+"");
		DashBoardBajasDTO maternidades = new DashBoardBajasDTO();
		maternidades.setBaja("maternidad");
		maternidades.setCantidad(bajasMaternidad.size()+"");
		resultado.add(bajas);
		resultado.add(maternidades);
		return resultado;
	}

}
