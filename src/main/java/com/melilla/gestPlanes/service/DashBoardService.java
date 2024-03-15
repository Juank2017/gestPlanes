package com.melilla.gestPlanes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.repository.CiudadanoRepository;

public interface DashBoardService {


	
	List<Object> ciudadanosPorEstado(Long idPlan);
	
}
