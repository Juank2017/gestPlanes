package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateNominaDTO {
	
	private long idProcedimiento;
	
	private long idContratoReclamado;
	
	private LocalDate fechaFin;
	
	private LocalDate fechaInicio;

	private double baseDevengada;
	
	private double basePercibida;
	
	private double baseReclamada;

	private double prorrataDevengada;
	
	private double prorrataPercibida;

	private double prorrataReclamada;
	
	private double residenciaDevengada;
	
	private double residenciaPercibida;
	
	private double residenciaReclamada;
	
}
