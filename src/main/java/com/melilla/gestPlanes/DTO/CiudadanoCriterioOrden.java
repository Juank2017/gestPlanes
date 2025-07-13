package com.melilla.gestPlanes.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO para recibir el criterio de orden (ascendente o descendente) en las
 * consultas de trabajadores.
 */
@Data
@RequiredArgsConstructor
public class CiudadanoCriterioOrden {

	private String id;
	private boolean desc;

}
