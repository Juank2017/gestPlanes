package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CrearParteBajaDTO {

	long idCiudadano;
	
	long idTipoContingencia;
	
	LocalDate fechaInicioBaja;
	
	//LocalDate fechaFinBaja;
	
	
	
}
