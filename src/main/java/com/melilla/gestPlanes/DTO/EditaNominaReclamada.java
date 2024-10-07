package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EditaNominaReclamada {
	
	private long idNomina;
	
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
