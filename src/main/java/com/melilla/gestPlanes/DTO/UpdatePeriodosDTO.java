package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdatePeriodosDTO {
	
	private long idProcedimiento;
	
	private long idContratoReclamado;
	
	private LocalDate fechaInicio;
	
	private LocalDate fechaFinal;
	
	
	
	private String gc;

}
