package com.melilla.gestPlanes.exceptions.exceptions;

public class VacacionesNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VacacionesNotFoundException(long idPeriodo) {
		super("Periodo no encontrado "+idPeriodo);
	}

}
