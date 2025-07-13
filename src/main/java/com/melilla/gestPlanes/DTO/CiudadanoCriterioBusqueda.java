package com.melilla.gestPlanes.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * DTO para recibir el campo de búsqueda y el valor a buscar en las consultas
 * del listado de trabajadores.
 */
@Data
@RequiredArgsConstructor
public class CiudadanoCriterioBusqueda {

	private String id;
	private String value;
}
