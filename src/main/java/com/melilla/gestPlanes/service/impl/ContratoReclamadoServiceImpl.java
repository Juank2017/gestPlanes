package com.melilla.gestPlanes.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.NominasReclamadas;
import com.melilla.gestPlanes.service.ContratoReclamadoService;
import com.melilla.gestPlanes.service.NominaReclamadaService;

@Service
public class ContratoReclamadoServiceImpl implements ContratoReclamadoService{

	@Override
	public BigDecimal totalReclamadoContrato(ContratoReclamado contrato) {

		List<NominasReclamadas> nominas = contrato.getNominasReclamadas();
		
		if (nominas.isEmpty()) return new BigDecimal("0");
		
		BigDecimal total = new BigDecimal("0");
		
		for (NominasReclamadas nomina : nominas) {
			
			BigDecimal devengado = nomina.getBaseDevengada().add(nomina.getProrrataDevengada()).add(nomina.getResidenciaDevengada());
			
			BigDecimal percibido = nomina.getBasePercibida().add(nomina.getProrrataPercibida()).add(nomina.getResidenciaPercibida());
			
			total =total.add(devengado.subtract(percibido));
			
			
		}
		
		return total;
	}

	@Override
	public BigDecimal totalPercibidoContrato(ContratoReclamado contrato) {
		List<NominasReclamadas> nominas = contrato.getNominasReclamadas();
		
		if (nominas.isEmpty()) return new BigDecimal("0");
		
		BigDecimal total = new BigDecimal("0");
		
		for (NominasReclamadas nomina : nominas) {
			
			total = total.add((nomina.getBasePercibida().add(nomina.getProrrataPercibida()).add(nomina.getResidenciaPercibida())));
			
			
		}
		
		return total;
	}

	@Override
	public BigDecimal totalReconocidoContrato(ContratoReclamado contrato) {
		List<NominasReclamadas> nominas = contrato.getNominasReclamadas();
		
		if (nominas.isEmpty()) return new BigDecimal("0");
		
		BigDecimal total = new BigDecimal("0");
		
		for (NominasReclamadas nomina : nominas) {
			
			total = total.add((nomina.getBaseCalculada().add(nomina.getProrrataCalculada()).add(nomina.getResidenciaCalculada())));
			
			
		}
		
		return total;
	}

	@Override
	public BigDecimal totalDevengadoContrato(ContratoReclamado contrato) {
		List<NominasReclamadas> nominas = contrato.getNominasReclamadas();
		
		if (nominas.isEmpty()) return new BigDecimal("0");
		
		BigDecimal total = new BigDecimal("0");
		
		for (NominasReclamadas nomina : nominas) {
			
			total = total.add((nomina.getBaseDevengada().add(nomina.getProrrataDevengada()).add(nomina.getResidenciaDevengada())));
					
			
			
		}
		
		return total;
	}

}
