package com.melilla.gestPlanes.exceptions.exceptions;

public class TrabajadorNoEsJefeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TrabajadorNoEsJefeException(String mensaje) {
		super(mensaje);
	}

}
