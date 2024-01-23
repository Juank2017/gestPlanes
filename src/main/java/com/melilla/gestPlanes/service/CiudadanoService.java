package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.model.Ciudadano;

public interface CiudadanoService {
	
	List<Ciudadano> getCiudadanos(Long idPlan);
	
	Ciudadano getCiudadano(Long idCiudadano);

	Ciudadano crearCiudadano(Ciudadano ciudadano);
	
	Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador);
	
	boolean existeTrabajador(String DNI);
	
	Page<Ciudadano>getTrabajadores(CiudadanoOrdenBusqueda ordenBusqueda);
	Optional<Ciudadano>getTrabajadorPorDNI(String DNI);
	
	Ciudadano editaTrabajador(UpdateTrabajadorDTO2 trabajador);
	
	List<Ciudadano>modificarEstado(List<ModificaEstadoDTO> trabajadores );
	
}
