package com.melilla.gestPlanes.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ListadoTrabajadoresConPartes;
import com.melilla.gestPlanes.DTO.ModificaEquipoDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoPrevencionDTO;
import com.melilla.gestPlanes.DTO.ModificaFechaContratoDTO;
import com.melilla.gestPlanes.DTO.ModificarOrganismoContrato;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.DTO.VacantesResponseDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.enums.TipoModificacionPrevencion;

public interface CiudadanoService {
	
	List<Ciudadano> getCiudadanos(Long idPlan);
	
	Ciudadano getCiudadano(Long idCiudadano);
	
	Ciudadano getTrabajadorByDNIAndEstado(String DNI,String estado);
	
	List<Ciudadano>getAllTrabajadorByDNIAndEstado(String DNI, String estado);
	List<Ciudadano>getAllTrabajadorByDNI(String DNI);

	Ciudadano crearCiudadano(Ciudadano ciudadano);
	
	Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador);
	
	boolean existeTrabajador(String DNI);
	
	boolean existeTrabajadorEnEstadoContratado(String DNI);
	
	Page<Ciudadano>getTrabajadores(CiudadanoOrdenBusqueda ordenBusqueda);
	
	long numeroTrabajadores(Plan plan, boolean deleted);
	
	Optional<Ciudadano>getTrabajadorPorDNI(String DNI);
	
	Ciudadano editaTrabajador(UpdateTrabajadorDTO2 trabajador);
	
	List<Ciudadano>modificarEstado(List<ModificaEstadoDTO> trabajadores );
	
	List<Ciudadano>modificarFechaContrato(List<ModificaFechaContratoDTO> trabajadores );
	
	List<Ciudadano>modificarOrganismoContrato(List<ModificarOrganismoContrato> trabajadores );
	
	List<Ciudadano>modificaEquipo(List<ModificaEquipoDTO>trabajadores);
	
	List<Ciudadano>modificaPrevencion(List<ModificaEstadoPrevencionDTO>trabajadores, String tipo);
	
	
	
	int trabajadoresContratadosOrganismoOcupacion(Long idOrganismo,Long idOcupacion,List<String>estados);
	
	int trabajadoresPrevistosOrganismoOcupacion(Long idOrganismo,Long idOcupacion);
	
	VacantesResponseDTO vacantesOrganismoOcupacion(Long idOrganismo,Long idOcupacion);
	
	List<VacantesResponseDTO>listadoVacantes();
	
	List<Ciudadano>trabajadoresConVacaciones(Long idPlan);
	
	void deleteTrabajador(Long idTrabajador);
	
	void restoreTrabajador(Long idTrabajador);
	
	List<Ciudadano> ciudadanosPorDNI(String dni);
	
	Ciudadano saveCiudadano(Ciudadano ciudadano);
	
	List<ListadoTrabajadoresConPartes> trabajadoresConPartesDebajaPlanActivo();
	
	//Comprueba si tiene algún parte de baja abierto.
	boolean estaDeBaja(long idTrabajador);
	
	boolean subirPlantilla(MultipartFile file);
	
	
}
