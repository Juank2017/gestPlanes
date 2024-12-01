package com.melilla.gestPlanes.exceptions.exceptions;

public class SalarioNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SalarioNotFoundException(Long long1) {
		super("Salario del grupo "+long1+" no encontrado");
	}
}
