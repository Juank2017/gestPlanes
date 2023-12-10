package com.melilla.gestPlanes.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.melilla.gestPlanes.model.Documento;

import lombok.Data;

@Data
public class GeneraContratoResponseDTO {

	Long idCiudadano;
	String Nombre;
	String Apellido1;
	String Apellido2;
	@JsonProperty("DNI")
	String DNI;
	Documento documento;
	
}
