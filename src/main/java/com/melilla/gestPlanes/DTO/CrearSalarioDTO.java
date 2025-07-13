package com.melilla.gestPlanes.DTO;

import lombok.Data;

/**
 * DTO para crear un salario del plan.
 */
@Data
public class CrearSalarioDTO {

	private String descripcion;
	private long idPlan;
}
