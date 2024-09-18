package com.melilla.gestPlanes.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.ProcedimientoDTO;
import com.melilla.gestPlanes.model.Abogado;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.repository.ProcedimientoRepository;
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

					List<ContratoReclamado> contratos = ciudadano.getContratosReclamados();

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

}
