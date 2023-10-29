package com.melilla.gestPlanes.exceptions.exceptions;

public class PlanNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PlanNotFoundException(String mensaje) {
		super(mensaje);
	}

}
