package com.melilla.gestPlanes.exceptions.exceptions;

public class CiudadanoNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CiudadanoNotFoundException(Long idCiudadano) {
		super("No existe el ciudadano con id: "+idCiudadano);
	}

}
