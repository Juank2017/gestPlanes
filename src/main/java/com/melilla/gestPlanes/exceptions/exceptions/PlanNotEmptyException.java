package com.melilla.gestPlanes.exceptions.exceptions;

public class PlanNotEmptyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PlanNotEmptyException(long idPlan) {
		super("El plan con id "+idPlan+" tiene registros creados y no se puede eliminar." );
	}

}
