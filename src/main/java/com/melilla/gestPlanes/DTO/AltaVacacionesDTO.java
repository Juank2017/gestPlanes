package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AltaVacacionesDTO {

	Long idCiudadano;
	LocalDate fechaInicio;
	LocalDate fechaFinal;
	
}
