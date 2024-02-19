package com.melilla.gestPlanes.exceptions.exceptions;

public class EquipoNoEncontradoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EquipoNoEncontradoException(Long idEquipo) {
		super("Equipo "+ idEquipo + " no encontrado");
	}

}
