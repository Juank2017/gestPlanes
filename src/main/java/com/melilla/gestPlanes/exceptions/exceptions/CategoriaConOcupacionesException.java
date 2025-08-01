package com.melilla.gestPlanes.exceptions.exceptions;

public class CategoriaConOcupacionesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CategoriaConOcupacionesException(Long idCategoria) {
		super("La categoría con id "+idCategoria+" no puede ser borrada por tener ocupaciones.");
	}

}
