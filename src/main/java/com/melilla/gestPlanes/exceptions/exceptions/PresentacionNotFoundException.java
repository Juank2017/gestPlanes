package com.melilla.gestPlanes.exceptions.exceptions;

public class PresentacionNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PresentacionNotFoundException() {
		super("Presentación no encontrada");
	}

}
