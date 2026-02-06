package com.melilla.gestPlanes.exceptions.exceptions;

public class GenericNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public GenericNotFoundException() {
		super("El recurso no ha sido encontrado.");
	}

}
