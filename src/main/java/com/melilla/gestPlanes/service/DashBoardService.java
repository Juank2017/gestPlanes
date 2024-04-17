package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.DashBoardBajasDTO;
import com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO;

public interface DashBoardService {


	
	List<DashBoardEstadoGeneroDTO> ciudadanosPorEstado(Long idPlan);
	
	List<DashBoardBajasDTO>trabajadoresEnBaja(Long idPlan);
}
