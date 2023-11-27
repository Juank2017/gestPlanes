package com.melilla.gestPlanes.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.DestinoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OrganismoNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.ContratoRepository;
import com.melilla.gestPlanes.repository.DestinoRepository;
import com.melilla.gestPlanes.repository.OrganismoRepository;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class CiudadanoServiceImpl implements CiudadanoService {
	
	@Autowired
	private CiudadanoRepository ciudadanoRepository;
	
	@Autowired
	private OrganismoRepository organismoRepository;
	
	@Autowired
	private DestinoRepository destinoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ContratoRepository contratoRepository;
	
	@Autowired
	private PlanService planService;

	@Override
	public List<Ciudadano> getCiudadanos(Long idPlan) {
		
		return ciudadanoRepository.findAllByIdPlanIdPlan(idPlan);
	}

	@Override
	public Optional<Ciudadano> getCiudadano(Long idCiudadano) {
		
		return ciudadanoRepository.findById(idCiudadano);
	}

	@Override
	public Ciudadano crearCiudadano(Ciudadano ciudadano) {
		
		return ciudadanoRepository.save(ciudadano);
	}

	@Override
	public Ciudadano crearTrabajador(CreateTrabajadorDTO trabajador) {
		
		
		Ciudadano nuevoCiudadano = ciudadanoRepository.save(
				Ciudadano.builder()
				.nombre(trabajador.getNombre().toUpperCase())
				.apellido1(trabajador.getApellido1().toUpperCase())
				.apellido2(trabajador.getApellido2().toUpperCase())
				.DNI(trabajador.getDNI().toUpperCase())
				.email(trabajador.getEmail())
				.esJefeEquipo(false)
				.telefono(trabajador.getTelefono())
				.sexo(trabajador.getSexo())
				.seguridadSocial(trabajador.getSeguridadSocial())
				.idPlan(planService.getPlanActivo())
				.fechaRegistro(trabajador.getFechaRegistro())
				.fechaNacimiento(trabajador.getFechaNacimiento())
				.estado(trabajador.getEstado())
				
				.build());
		Contrato nuevoContrato =contratoRepository.save( Contrato
				.builder()
				.base(trabajador.getBase())
				.prorratas(trabajador.getProrratas())
				.residencia(trabajador.getResidencia())
				.total(trabajador.getTotal())
				.entidad(organismoRepository.findById(trabajador.getEntidad()).orElseThrow(()->new OrganismoNotFoundException(trabajador.getEntidad())))
				.destino(destinoRepository.findById(trabajador.getDestino()).orElseThrow(()->new DestinoNotFoundException(trabajador.getDestino())))
				.categoria(categoriaRepository.findById(trabajador.getCategoria()).orElseThrow(()->new CategoriaNotFoundException(trabajador.getCategoria())))
				.diasVacaciones(trabajador.getDuracion() == 6 ? 15:null)
				.duracion(trabajador.getDuracion())
				.fechaInicio(trabajador.getFechaInicio())
				.fechaFinal(trabajador.getFechaFinal())
				.turno(trabajador.getTurno())
				.porcentajeHoras("63")
				.gc(trabajador.getGc().toString())
				.ciudadano(nuevoCiudadano)
				.build());
		//log.info(nuevoContrato.toString());

		
		
		return nuevoCiudadano;
	}

	@Override
	public boolean existeTrabajador(String DNI) {
	
		return ciudadanoRepository.existsByDNI(DNI);
	}

	
	
	

}
