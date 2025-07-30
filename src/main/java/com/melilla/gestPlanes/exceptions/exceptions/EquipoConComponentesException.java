package com.melilla.gestPlanes.exceptions.exceptions;

public class EquipoConComponentesException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public EquipoConComponentesException(long idEquipo) {
		super("El equipo con id "+idEquipo+" tiene componentes y no puede ser eliminado");
	}

}
