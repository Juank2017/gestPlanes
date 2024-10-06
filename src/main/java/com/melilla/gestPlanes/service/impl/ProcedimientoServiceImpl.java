package com.melilla.gestPlanes.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreatePeriodosReclamadosDTO;
import com.melilla.gestPlanes.DTO.CreateProcedimientoDTO;

import com.melilla.gestPlanes.DTO.ProcedimientoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.ProcedimientoNotFoundException;
import com.melilla.gestPlanes.model.Abogado;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.repository.ProcedimientoRepository;
import com.melilla.gestPlanes.service.AbogadoService;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.ContratoReclamadoService;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcedimientoServiceImpl implements ProcedimientoService {

	@Autowired
	ProcedimientoRepository procedimientoRepository;

	@Autowired
	ContratoReclamadoService contratoReclamadoService;
	
	@Autowired
	CiudadanoService ciudadanoService;
	
	@Autowired
	AbogadoService abogadoService;

	@Override
	public List<ProcedimientoDTO> obtenerProcedimientos() {

		BigDecimal totalDevengado = new BigDecimal("0");
		BigDecimal totalPercibido = new BigDecimal("0");
		BigDecimal totalReclamado = new BigDecimal("0");
		BigDecimal totalReconocido = new BigDecimal("0");

		List<Procedimiento> procedimientos = procedimientoRepository.findAll();

		List<ProcedimientoDTO> resultado = new ArrayList<ProcedimientoDTO>();

		if (procedimientos.isEmpty()) {
			return resultado;
		} else {
			for (Procedimiento procedimiento : procedimientos) {

				ProcedimientoDTO procedimientoDTO = new ProcedimientoDTO();

				procedimientoDTO.setIdProcedimiento(procedimiento.getIdProcedimiento());
				procedimientoDTO.setNumeroProcedimiento(procedimiento.getNumeroProcedimiento());
				procedimientoDTO.setSentencia(procedimiento.getSentencia());

				if (procedimiento.getAbogado() != null) {
					Abogado abogado = procedimiento.getAbogado();
					procedimientoDTO.setIdAbogado(abogado.getIdAbogado());
					procedimientoDTO.setApellido1Abogado(abogado.getApellido1());
					procedimientoDTO.setApellido2Abogado(abogado.getApellido2());
					procedimientoDTO.setEmail(abogado.getEmail());
					procedimientoDTO.setNombreAbogado(abogado.getNombre());
					procedimientoDTO.setNumeroColegiado(abogado.getNumeroColegiado());
					procedimientoDTO.setTelefono(abogado.getTelefono());
				}

				Ciudadano ciudadano = procedimiento.getCiudadano();

				if (ciudadano != null) {
					
					
					procedimientoDTO.setNombre(ciudadano.getNombre());
					procedimientoDTO.setApellido1(ciudadano.getApellido1());
					procedimientoDTO.setApellido2(ciudadano.getApellido2());
					procedimientoDTO.setDNI(ciudadano.getDNI());
					procedimientoDTO.setSeguridadSocial(ciudadano.getSeguridadSocial());

					List<ContratoReclamado> contratos = procedimiento.getPeriodos();

					if (!contratos.isEmpty()) {

						for (ContratoReclamado contrato : contratos) {

							totalDevengado = totalDevengado
									.add(contratoReclamadoService.totalDevengadoContrato(contrato));
							totalPercibido = totalPercibido
									.add(contratoReclamadoService.totalPercibidoContrato(contrato));
							totalReclamado = totalReclamado
									.add(contratoReclamadoService.totalReclamadoContrato(contrato));
							totalReconocido = totalReconocido
									.add(contratoReclamadoService.totalReconocidoContrato(contrato));

						}
					}

				}

				procedimientoDTO.setTotalReconocido(totalReconocido);
				procedimientoDTO.setTotalPercibido(totalPercibido);
				procedimientoDTO.setTotalReclamado(totalReclamado);
				procedimientoDTO.setTotalDevengado(totalDevengado);
				procedimientoDTO.setTotalAbonado(new BigDecimal("0"));
				resultado.add(procedimientoDTO);
			}
		}

		return resultado;
	}

	@Override
	public List<Procedimiento> procedimientos() {

		return procedimientoRepository.findAll();
	}

	@Override
	public Procedimiento crearProcedimiento(CreateProcedimientoDTO procedimientDTO) {
		
		Ciudadano demandante = new Ciudadano();
		
		Abogado abogado = abogadoService.getAbogado(procedimientDTO.getRepresentante());
		
		
		if (ciudadanoService.existeTrabajador(procedimientDTO.getDni())) {
			List<Ciudadano>demandantes = ciudadanoService.ciudadanosPorDNI(procedimientDTO.getDni()); 
			if(!demandantes.isEmpty()) {
				demandante = demandantes.get(0);
			}
		}else {
			demandante.setNombre(procedimientDTO.getNombreTrabajador());
			demandante.setApellido1(procedimientDTO.getApellido1Trabajador());
			demandante.setApellido2(procedimientDTO.getApellido2Trabajador());
			demandante.setDNI(procedimientDTO.getDni());
			demandante.setSeguridadSocial(procedimientDTO.getSsTrabajador());
			
			demandante = ciudadanoService.crearCiudadano(demandante);
		}
		
		Procedimiento nuevoProcedimiento = new Procedimiento();
		
		nuevoProcedimiento.setAbogado(abogado);
		nuevoProcedimiento.setCiudadano(demandante);
		nuevoProcedimiento.setNumeroProcedimiento(procedimientDTO.getNumeroProcedimiento());
		nuevoProcedimiento.setSentencia(procedimientDTO.getSentencia());
		
		nuevoProcedimiento = procedimientoRepository.save(nuevoProcedimiento);
		
		List<ContratoReclamado> periodos = new ArrayList<>();
		
		List<CreatePeriodosReclamadosDTO> periodosReclamados = procedimientDTO.getPeriodos();
		
		if (!periodosReclamados.isEmpty()) {
			for (CreatePeriodosReclamadosDTO createPeriodosReclamadosDTO : periodosReclamados) {
				
				ContratoReclamado periodo = new ContratoReclamado();
				
				periodo.setFechaInicio(createPeriodosReclamadosDTO.getFechaInicio());
				periodo.setFechaFinal(createPeriodosReclamadosDTO.getFechaFin());
				periodo.setProcedimiento(nuevoProcedimiento);
				periodo.setGc(createPeriodosReclamadosDTO.getGc());
				
				
				periodo = contratoReclamadoService.crearContratoReclamado(periodo);
				
				periodos.add(periodo);
				
			}
		}
		

		nuevoProcedimiento.setPeriodos(periodos);
		
		
		
		
		return nuevoProcedimiento;
	}

	@Override
	public Procedimiento getProcedimiento(Long idProcedimiento) {
		
		return procedimientoRepository.findById(idProcedimiento).orElseThrow( ()-> new ProcedimientoNotFoundException(idProcedimiento) );
	}

}
