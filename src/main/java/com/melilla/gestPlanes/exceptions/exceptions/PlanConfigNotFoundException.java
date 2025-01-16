package com.melilla.gestPlanes.exceptions.exceptions;

public class PlanConfigNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PlanConfigNotFoundException(long idPlan) {
		super("No se ha encontrado el plan "+idPlan);
	}

}
