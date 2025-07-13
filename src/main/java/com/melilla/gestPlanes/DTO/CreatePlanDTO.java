package com.melilla.gestPlanes.DTO;

import lombok.Data;

/**
 * DTO para crear un plan.
 */
@Data
public class CreatePlanDTO {

	
	private String denominacion;
	private boolean activo;
}
