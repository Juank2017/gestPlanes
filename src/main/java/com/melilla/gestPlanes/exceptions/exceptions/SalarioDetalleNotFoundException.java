package com.melilla.gestPlanes.exceptions.exceptions;

public class SalarioDetalleNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SalarioDetalleNotFoundException(long id) {
		super("No se encuentra "+id);
	}

}
