package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

/**
 * DTO para recibir los datos de un período de vacaciones de un trabajador desde
 * el front.
 */
@Data
public class AltaVacacionesDTO {

	LocalDate fechaInicio;

	LocalDate fechaFinal;
	
	int dias;
	
	Long idCiudadano;
}
