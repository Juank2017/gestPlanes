package com.melilla.gestPlanes.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreatePeriodosReclamadosDTO;
import com.melilla.gestPlanes.DTO.CreateProcedimientoDTO;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ProcedimientoDTO;
import com.melilla.gestPlanes.DTO.UpdatePeriodosDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
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
		nuevoProcedimiento.setFechaSentencia(procedimientDTO.getFechaSentencia());
		
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

	@Override
	public Procedimiento updateContratoReclamadoProcedimiento(UpdatePeriodosDTO periodo) {
	
		Procedimiento procedimiento = procedimientoRepository.findById(periodo.getIdProcedimiento()).orElseThrow(()->new ProcedimientoNotFoundException(periodo.getIdProcedimiento()));
		ContratoReclamado contrato = new ContratoReclamado();
		
		if(periodo.getIdContratoReclamado() == 0) {
			
			contrato.setFechaInicio(periodo.getFechaInicio());
			contrato.setFechaFinal(periodo.getFechaFinal());
			contrato.setGc(periodo.getGc());
			contrato.setProcedimiento(procedimiento);
			contrato = contratoReclamadoService.crearContratoReclamado(contrato);
			
			procedimiento.getPeriodos().add(contrato);
			
		}else {
			contrato = contratoReclamadoService.getContrato(periodo.getIdContratoReclamado());
			contrato.setFechaFinal(periodo.getFechaFinal());
			contrato.setFechaInicio(periodo.getFechaInicio());
			contrato.setGc(periodo.getGc());
			contratoReclamadoService.updateContrato(contrato);
		}
			
		return procedimientoRepository.save(procedimiento);
	}

	@Override
	public Procedimiento updateProcedimiento(ProcedimientoDTO procedimiento) {
	
		Procedimiento procedimientoBBDD = procedimientoRepository
				.findById(procedimiento.getIdProcedimiento()).orElseThrow(()->new ProcedimientoNotFoundException(procedimiento.getIdProcedimiento()));
		
		procedimientoBBDD.setAbogado(abogadoService.getAbogado(procedimiento.getIdAbogado()));
		
		procedimientoBBDD.setNumeroProcedimiento(procedimiento.getNumeroProcedimiento());
		procedimientoBBDD.setSentencia(procedimiento.getSentencia());
		procedimientoBBDD.setFechaSentencia(procedimiento.getFechaSentencia());
		
		if (ciudadanoService.existeTrabajador(procedimiento.getDNI())) {
			Ciudadano ciudadanoBBDD = ciudadanoService.getTrabajadorPorDNI(procedimiento.getDNI()).orElseThrow(()-> new CiudadanoNotFoundException(0l));
			ciudadanoBBDD.setNombre(procedimiento.getNombre());
			ciudadanoBBDD.setApellido1(procedimiento.getApellido1());
			ciudadanoBBDD.setApellido2(procedimiento.getApellido2());
			ciudadanoBBDD.setDNI(procedimiento.getDNI());
			ciudadanoBBDD.setSeguridadSocial(procedimiento.getSeguridadSocial());
			ciudadanoService.saveCiudadano(ciudadanoBBDD);
			procedimientoBBDD.setCiudadano(ciudadanoBBDD);
			
		} else {
			procedimientoBBDD.setCiudadano(
					ciudadanoService.crearTrabajador(
							CreateTrabajadorDTO.builder()
								.nombre(procedimiento.getNombre())
								.apellido1(procedimiento.getApellido1())
								.apellido2(procedimiento.getApellido2())
								.DNI(procedimiento.getDNI())
								.seguridadSocial(procedimiento.getSeguridadSocial())
								.build()
							)
					);
		}

		
		
		
		return procedimientoRepository.saveAndFlush(procedimientoBBDD) ;
	}

	@Override
	public BigDecimal totalReconocidoProcedimiento(long idProcedimiento) {
		BigDecimal total= BigDecimal.ZERO;
		
		
		Procedimiento procedimiento = procedimientoRepository.findById(idProcedimiento).orElseThrow(()->new ProcedimientoNotFoundException(idProcedimiento));
		
		List<ContratoReclamado> contratos = procedimiento.getPeriodos();
		
		for (ContratoReclamado contratoReclamado : contratos) {
			
			total.add(contratoReclamadoService.totalReconocidoContrato(contratoReclamado));
		}
		
		
		return total;
	}

}
