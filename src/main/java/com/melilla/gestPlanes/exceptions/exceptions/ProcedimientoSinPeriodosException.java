package com.melilla.gestPlanes.exceptions.exceptions;

public class ProcedimientoSinPeriodosException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ProcedimientoSinPeriodosException(long idProcedimiento) {
		super("El procedimiento "+idProcedimiento+" no tiene contratos.");
	}

}
