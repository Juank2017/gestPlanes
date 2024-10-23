package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.ModificaFechaContratoDTO;
import com.melilla.gestPlanes.DTO.ModificarOrganismoContrato;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.DTO.VacantesResponseDTO;
import com.melilla.gestPlanes.model.Ciudadano;

public interface CiudadanoService {
	
	List<Ciudadano> getCiudadanos(Long idPlan);
	
	Ciudadano getCiudadano(Long idCiudadano);
	
	Ciudadano getTrabajadorByDNIAndEstado(String DNI,String estado);
	
	List<Ciudadano>getAllTrabajadorByDNIAndEstado(String DNI, String estado);

	Ciudadano crearCiudadano(Ciudadano ciudadano);
	
	Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador);
	
	boolean existeTrabajador(String DNI);
	
	boolean existeTrabajadorEnEstadoContratado(String DNI);
	
	Page<Ciudadano>getTrabajadores(CiudadanoOrdenBusqueda ordenBusqueda);
	Optional<Ciudadano>getTrabajadorPorDNI(String DNI);
	
	Ciudadano editaTrabajador(UpdateTrabajadorDTO2 trabajador);
	
	List<Ciudadano>modificarEstado(List<ModificaEstadoDTO> trabajadores );
	
	List<Ciudadano>modificarFechaContrato(List<ModificaFechaContratoDTO> trabajadores );
	
	List<Ciudadano>modificarOrganismoContrato(List<ModificarOrganismoContrato> trabajadores );
	
	int trabajadoresContratadosOrganismoOcupacion(Long idOrganismo,Long idOcupacion,List<String>estados);
	
	int trabajadoresPrevistosOrganismoOcupacion(Long idOrganismo,Long idOcupacion);
	
	VacantesResponseDTO vacantesOrganismoOcupacion(Long idOrganismo,Long idOcupacion);
	
	List<VacantesResponseDTO>listadoVacantes();
	
	List<Ciudadano>trabajadoresConVacaciones(Long idPlan);
	
	void deleteTrabajador(Long idTrabajador);
	
	void restoreTrabajador(Long idTrabajador);
	
	List<Ciudadano> ciudadanosPorDNI(String dni);
	
	Ciudadano saveCiudadano(Ciudadano ciudadano);
}
