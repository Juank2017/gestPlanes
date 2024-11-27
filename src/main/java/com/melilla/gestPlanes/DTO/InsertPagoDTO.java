package com.melilla.gestPlanes.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class InsertPagoDTO {

	
	private long idProcedimiento;
	
	private LocalDate fecha;
	
	BigDecimal importe;
	
}
