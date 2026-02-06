package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EditaParteBajaDTO {
	
	long idParteBaja;
	
	long idCiudadano;
	
	long idTipoContingencia;
	
	LocalDate fechaInicioBaja;
	
	LocalDate fechaFinBaja;

}
