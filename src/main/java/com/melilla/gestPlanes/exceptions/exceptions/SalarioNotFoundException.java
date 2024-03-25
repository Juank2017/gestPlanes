package com.melilla.gestPlanes.exceptions.exceptions;

public class SalarioNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SalarioNotFoundException(int grupo) {
		super("Salario del grupo "+grupo+" no encontrado");
	}
}
