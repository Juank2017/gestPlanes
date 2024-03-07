package com.melilla.gestPlanes.exceptions.exceptions;

public class NotaNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotaNotFoundException(String mensaje) {
		super(mensaje);
	}
}
