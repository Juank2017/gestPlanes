package com.melilla.gestPlanes.service;

import java.math.BigDecimal;

import com.melilla.gestPlanes.DTO.CreateNominaDTO;
import com.melilla.gestPlanes.model.ContratoReclamado;

public interface ContratoReclamadoService {

	BigDecimal totalReclamadoContrato(ContratoReclamado contrato);
	
	BigDecimal totalPercibidoContrato(ContratoReclamado contrato);
	
	BigDecimal totalReconocidoContrato(ContratoReclamado contrato);
	
	BigDecimal totalDevengadoContrato(ContratoReclamado contrato);
	
	ContratoReclamado crearContratoReclamado(ContratoReclamado contrato);
	
	ContratoReclamado insertarNominaEnContrato(ContratoReclamado contrato,CreateNominaDTO nomina);
	
	ContratoReclamado getContrato(Long idContrato);
	
}
