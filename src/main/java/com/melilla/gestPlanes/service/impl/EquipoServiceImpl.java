package com.melilla.gestPlanes.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.ComponentesEquipoDTO;
import com.melilla.gestPlanes.DTO.CreateEquipoDTO;
import com.melilla.gestPlanes.DTO.EditaEquipoDTO;
import com.melilla.gestPlanes.DTO.EquipoResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.ComponenteEquipoDuplicadoException;
import com.melilla.gestPlanes.exceptions.exceptions.EquipoConComponentesException;
import com.melilla.gestPlanes.exceptions.exceptions.EquipoCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.EquipoNoEncontradoException;
import com.melilla.gestPlanes.exceptions.exceptions.TrabajadorNoEsJefeException;
import com.melilla.gestPlanes.mappers.ComponenteDTMapper;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.EquipoRepository;
import com.melilla.gestPlanes.service.EquipoService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.Data;
import lombok.extern.java.Log;

@Service
@Data
@Log
public class EquipoServiceImpl implements EquipoService {

	@Autowired
	private EquipoRepository equipoRepository;

	@Autowired
	private CiudadanoRepository ciudadanoRepository;

	@Autowired
	private PlanService planService;
	
	//@Autowired
	//private final ComponenteDTMapper mapper;

	@Override
	public List<EquipoResponseDTO> equipos(Long idPlan) {

		List<EquipoResponseDTO> listEquipos = new ArrayList<EquipoResponseDTO>();
		List<Equipo> equipos = equipoRepository.findAllByIdPlanIdPlan(idPlan);
		
		
		
		for (Equipo equipo : equipos) {
			
			EquipoResponseDTO eq = new EquipoResponseDTO();
			
			eq.setIdEquipo(equipo.getIdEquipo());
			eq.setNombreEquipo(equipo.getNombreEquipo());
			eq.setNombreJefe(equipo.getJefeEquipo().getNombre());
			eq.setApellido1Jefe(equipo.getJefeEquipo().getApellido1());
			eq.setApellido2Jefe(equipo.getJefeEquipo().getApellido2());
			eq.setDNIJefe(equipo.getJefeEquipo().getDNI());
			eq.setTelefonoJefe(equipo.getJefeEquipo().getTelefono());
			eq.setComponentes(equipo.getComponentes().stream().map((c)->ComponentesEquipoDTO.ciudadanoToComponentesEquipoDTO( c)).toList());
			eq.setDeleted(equipo.isDeleted());
			listEquipos.add(eq);
			
			
			
		}
		
		
		
		
		return listEquipos;
	}

	@Override
	public Equipo crearEquipo(CreateEquipoDTO equipo) {

		Equipo e = new Equipo();
		try {
			e.setNombreEquipo(equipo.getNombreEquipo());
			if (equipo.getDNI() != null) {
				Ciudadano jefe = ciudadanoRepository.findByDNIAndEstadoAndIdPlan( equipo.getDNI(),"CONTRATADO/A",planService.getWorikingPlan()).orElseThrow(()-> new CiudadanoNotFoundException(null));
				if (jefe == null) {
					throw new CiudadanoNotFoundException(1l);

				} else {
					if (!jefe.isEsJefeEquipo()) {
						throw new TrabajadorNoEsJefeException(
								"El trabajador con DNI: " + equipo.getDNI() + " no es Jefe de equipo");
					} else {
						e.setJefeEquipo(jefe);
					}
				}
			} else {
				e.setJefeEquipo(null);
			}

			e.setIdPlan(planService.getWorikingPlan());
			return equipoRepository.save(e);
		} catch (IncorrectResultSizeDataAccessException e2) {
			log.warning(e2.getMessage());
			throw new EquipoCreationException("No se ha podido crear. El DNI aparece en la BBDD más de una vez en estado CONTRATADO/A");
		}
		
	
	}

	@Override
	public Equipo editarEquipo(EditaEquipoDTO equipo) {

		Equipo equipoAEditar = equipoRepository.findById(equipo.getIdEquipo())
				.orElseThrow(() -> new EquipoNoEncontradoException(equipo.getIdEquipo()));

		equipoAEditar.setNombreEquipo(equipo.getNombreEquipo());

		List<Ciudadano> nuevojefeEquipo = ciudadanoRepository.findAllByDNIAndEstado(equipo.getDNI(), "CONTRATADO/A");
		if (nuevojefeEquipo.isEmpty()) {
			throw new CiudadanoNotFoundException(1l);
		} else {
			if (nuevojefeEquipo.size() > 1) {
				throw new ComponenteEquipoDuplicadoException(equipo.getDNI());
			} else {
				Ciudadano ciudadano = nuevojefeEquipo.get(0);

				ciudadano.setEsJefeEquipo(true);
				ciudadano.setEquipo(null);
				ciudadanoRepository.saveAndFlush(ciudadano);
				equipoAEditar.setJefeEquipo(ciudadano);

			}
		}

		return equipoRepository.saveAndFlush(equipoAEditar);
	}

	@Override
	public Equipo addComponente(Equipo equipo, Ciudadano ciudadano) {
		if(ciudadano.isEsJefeEquipo())throw new ComponenteEquipoDuplicadoException("El trabajador es Jefe de Equipo. No se puede agregar un Jefe como componete de equipo");
		if (equipo.getComponentes() != null && equipo.getComponentes().contains(ciudadano))
			throw new ComponenteEquipoDuplicadoException("Ya existe el trabajador " + ciudadano.getNombre() + " "
					+ ciudadano.getApellido1() + " en el equipo.");
		
		ciudadano.setEquipo(equipo);
		ciudadanoRepository.saveAndFlush(ciudadano);
		equipo.getComponentes().add(ciudadano);
		return equipoRepository.saveAndFlush(equipo);
	}

	@Override
	public Equipo removeComponente(Equipo equipo, Ciudadano ciudadano) {

		log.warning("contiene " + equipo.getComponentes().contains(ciudadano));
		equipo.getComponentes().remove(ciudadano);
		log.warning("contiene " + equipo.getComponentes().contains(ciudadano));

		ciudadano.setEquipo(null);
		ciudadanoRepository.saveAndFlush(ciudadano);
		return equipoRepository.saveAndFlush(equipo);
	}

	@Override
	public void eliminaEquipo(Long idEquipo) {
		
		Equipo equipo = equipoRepository.findById(idEquipo).orElseThrow(()-> new EquipoNoEncontradoException(idEquipo));
		
		if (!equipo.getComponentes().isEmpty()) {
			throw new EquipoConComponentesException(idEquipo);
		}else {
			equipoRepository.delete(equipo);
		}

	}

	@Override
	public EquipoResponseDTO equipoToDTO(Long idPlan, Long idEquipo) {
		
		EquipoResponseDTO response = new EquipoResponseDTO();
		
		Equipo equipo = equipoRepository.findByIdPlanIdPlanAndIdEquipo(idPlan, idEquipo)
				.orElseThrow(() -> new EquipoNoEncontradoException(idEquipo));
		
		response.setIdEquipo(equipo.getIdEquipo());
		response.setNombreEquipo(equipo.getNombreEquipo());
		response.setNombreJefe(equipo.getJefeEquipo().getNombre());
		response.setApellido1Jefe(equipo.getJefeEquipo().getApellido1());
		response.setApellido2Jefe(equipo.getJefeEquipo().getApellido2());
		response.setDNIJefe(equipo.getJefeEquipo().getDNI());		
		response.setTelefonoJefe(equipo.getJefeEquipo().getTelefono());
		
		response.setComponentes(equipo.getComponentes().stream().map((c)-> ComponentesEquipoDTO.ciudadanoToComponentesEquipoDTO( c)).toList());

		return response;
	}

	@Override
	public Equipo equipo(Long idPlan, Long idEquipo) {
		
		return equipoRepository.findByIdPlanIdPlanAndIdEquipo(idPlan, idEquipo)
				.orElseThrow(() -> new EquipoNoEncontradoException(idEquipo));
	}

}
