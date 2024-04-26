package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ModificaFechaContratoDTO {
	
	private Long idCiudadano;
	private String dni;
	private LocalDate fechaInicio;
	private LocalDate fechaFinal;

}
