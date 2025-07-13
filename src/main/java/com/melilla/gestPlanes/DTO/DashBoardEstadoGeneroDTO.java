package com.melilla.gestPlanes.DTO;

import lombok.Data;

/**
 * DTO para enviar al front datos estadísticos por estado.
 */
@Data
public class DashBoardEstadoGeneroDTO {

	private String estado;
	private Long hombres;
	private Long mujeres;
}
