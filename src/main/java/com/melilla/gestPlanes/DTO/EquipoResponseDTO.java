package com.melilla.gestPlanes.DTO;

import java.util.List;

import com.melilla.gestPlanes.model.Ciudadano;

import lombok.Data;

@Data
public class EquipoResponseDTO {

	private Long idEquipo;
	private String nombreEquipo;
	private String nombreJefe;
	private String apellido1Jefe;
	private String apellido2Jefe;
	private String DNIJefe;
	private String telefonoJefe;
	private List<Ciudadano>componentes;
	
}
