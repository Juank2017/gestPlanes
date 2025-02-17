package com.melilla.gestPlanes.exceptions.exceptions;

public class PlanConfigErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PlanConfigErrorException() {
		super ("Hay un error en la configuración del plan.");
	}

}
