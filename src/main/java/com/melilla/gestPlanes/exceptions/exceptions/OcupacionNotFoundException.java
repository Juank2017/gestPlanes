package com.melilla.gestPlanes.exceptions.exceptions;

public class OcupacionNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OcupacionNotFoundException(Long idOcupacion) {
		super("No se encuentra la ocupación con id: "+idOcupacion);
	}

}
