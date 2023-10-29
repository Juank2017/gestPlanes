package com.melilla.gestPlanes.exceptions.exceptions;

public class MyFileNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyFileNotFoundException(String mensaje) {
		super(mensaje);
	}

}
