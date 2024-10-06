package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreatePeriodosReclamadosDTO {

	private LocalDate fechaInicio;
	
	private LocalDate fechaFin;
	
	private String jornada;
	
	private String gc;
	
}


//"periodos": [
//{
//  "fechaInicio": "2024-08-28",
//  "fechaFin": "2024-09-12",
//  "jornada": "",
//  "gc": "8"
//}
//]