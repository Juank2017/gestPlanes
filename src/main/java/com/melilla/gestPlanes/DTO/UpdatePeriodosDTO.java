package com.melilla.gestPlanes.DTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdatePeriodosDTO {
	
	private long idProcedimiento;
	
	private long idContratoReclamado;
	
	private LocalDate fechaInicio;
	
	private LocalDate fechaFinal;
	
	private BigDecimal totalDevengado = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	private BigDecimal totalRecibido = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	private BigDecimal totalReclamado = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	private BigDecimal totalReconocido = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	private BigDecimal totalAbonado = new BigDecimal("0").setScale(2,RoundingMode.HALF_DOWN );
	
	
	private String gc;

}
