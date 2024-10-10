package com.melilla.gestPlanes.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CreateNominaDTO;
import com.melilla.gestPlanes.exceptions.exceptions.ContratoReclamadoNotFoundException;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.NominasReclamadas;
import com.melilla.gestPlanes.repository.ContratoReclamadoRepository;
import com.melilla.gestPlanes.service.ContratoReclamadoService;
import com.melilla.gestPlanes.service.NominaReclamadaService;

@Service
public class ContratoReclamadoServiceImpl implements ContratoReclamadoService{
	
	@Autowired
	ContratoReclamadoRepository contratoReclamadoRepository;
	
	@Autowired
	NominaReclamadaService nominasReclamadasService;

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

	@Override
	public ContratoReclamado crearContratoReclamado(ContratoReclamado contrato) {
		
		return contratoReclamadoRepository.save(contrato);
	}

	@Override
	public ContratoReclamado insertarNominaEnContrato(ContratoReclamado contrato,CreateNominaDTO nomina) {
		
		
		NominasReclamadas nominaReclamada = new NominasReclamadas();
		
		nominaReclamada.setBaseDevengada( BigDecimal.valueOf(nomina.getBaseDevengada()).setScale(2,RoundingMode.HALF_DOWN ));
		nominaReclamada.setProrrataDevengada(BigDecimal.valueOf(nomina.getProrrataDevengada()).setScale(2, RoundingMode.HALF_DOWN));
		nominaReclamada.setResidenciaDevengada(BigDecimal.valueOf(nomina.getResidenciaDevengada()).setScale(2, RoundingMode.HALF_DOWN));
		
		nominaReclamada.setBasePercibida(BigDecimal.valueOf(nomina.getBasePercibida()).setScale(2,RoundingMode.HALF_DOWN ));
		nominaReclamada.setProrrataPercibida(BigDecimal.valueOf(nomina.getProrrataPercibida()).setScale(2, RoundingMode.HALF_DOWN));
		nominaReclamada.setResidenciaPercibida(BigDecimal.valueOf(nomina.getResidenciaPercibida()).setScale(2, RoundingMode.HALF_DOWN));
		
		
		nominaReclamada.setBaseReclamada(BigDecimal.valueOf(nomina.getBaseReclamada()).setScale(2,RoundingMode.HALF_DOWN ));
		nominaReclamada.setProrrataReclamada(BigDecimal.valueOf(nomina.getProrrataReclamada()).setScale(2, RoundingMode.HALF_DOWN));
		nominaReclamada.setResidenciaReclamada(BigDecimal.valueOf(nomina.getResidenciaReclamada()).setScale(2, RoundingMode.HALF_DOWN));
		
		nominaReclamada.setDiferenciaCalculada(BigDecimal.ZERO);
		nominaReclamada.setDiferenciaReclamada(BigDecimal.ZERO);
		
		nominaReclamada.setBaseCalculada(BigDecimal.ZERO);
		nominaReclamada.setProrrataCalculada(BigDecimal.ZERO);
		nominaReclamada.setResidenciaCalculada(BigDecimal.ZERO);
		
		
		
		nominaReclamada.setContrato(contrato);
		
		nominaReclamada.setFechaInicio(nomina.getFechaInicio());
		nominaReclamada.setFechaFin(nomina.getFechaFin());
		
		NominasReclamadas nominaInsertadaBBDD = nominasReclamadasService.insertarNomina(nominaReclamada);
		
		contrato.getNominasReclamadas().add(nominaInsertadaBBDD);
		
		
		
		
		
		return contratoReclamadoRepository.save(contrato);
	}

	@Override
	public ContratoReclamado getContrato(Long idContrato) {
		
		return contratoReclamadoRepository.findById(idContrato).orElseThrow( ()->new ContratoReclamadoNotFoundException( idContrato)  )  ;
	}

	@Override
	public ContratoReclamado updateContrato(ContratoReclamado contrato) {
		
		return contratoReclamadoRepository.saveAndFlush(contrato);
	}

}
