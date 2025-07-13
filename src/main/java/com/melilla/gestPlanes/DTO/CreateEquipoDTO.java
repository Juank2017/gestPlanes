package com.melilla.gestPlanes.DTO;

import lombok.Data;
/**
 * DTO para crear un equipo.
 */
@Data
public class CreateEquipoDTO {

	private String nombreEquipo;
	private String DNI;
	private Long idPlan;
}
