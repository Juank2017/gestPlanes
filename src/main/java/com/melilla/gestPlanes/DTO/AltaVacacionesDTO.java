package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AltaVacacionesDTO {

	
	LocalDate fechaInicio;
	
	LocalDate fechaFinal;
	int dias;
	Long idCiudadano;
}
