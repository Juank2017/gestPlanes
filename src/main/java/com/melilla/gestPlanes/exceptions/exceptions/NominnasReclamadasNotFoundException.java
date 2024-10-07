package com.melilla.gestPlanes.exceptions.exceptions;

public class NominnasReclamadasNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NominnasReclamadasNotFoundException(long idNomina) {
		super("No se encuentra la nómina con id: "+idNomina);
	}

}
