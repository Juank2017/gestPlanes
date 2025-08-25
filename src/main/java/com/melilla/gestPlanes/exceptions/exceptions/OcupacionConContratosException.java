package com.melilla.gestPlanes.exceptions.exceptions;

public class OcupacionConContratosException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OcupacionConContratosException(long idOcupacion) {
		super("La ocupación con id "+ idOcupacion+" no se puede eliminar al tener contratos.");
	}

}
