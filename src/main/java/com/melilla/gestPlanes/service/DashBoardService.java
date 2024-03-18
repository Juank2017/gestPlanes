package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.melilla.gestPlanes.DTO.DashBoardBajasDTO;
import com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;

public interface DashBoardService {


	
	List<DashBoardEstadoGeneroDTO> ciudadanosPorEstado(Long idPlan);
	
	List<DashBoardBajasDTO>trabajadoresEnBaja(Long idPlan);
}
