package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ModificaEstadoDTO {
	
	private String DNI;
	private Long idCiudadano;
	private String estado;
	private LocalDate fecha;

}
