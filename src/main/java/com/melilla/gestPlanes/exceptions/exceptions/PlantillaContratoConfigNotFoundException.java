package com.melilla.gestPlanes.exceptions.exceptions;

public class PlantillaContratoConfigNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlantillaContratoConfigNotFoundException(long id) {
		super("No se encuentra la plantilla con id: "+id);
	}
}
