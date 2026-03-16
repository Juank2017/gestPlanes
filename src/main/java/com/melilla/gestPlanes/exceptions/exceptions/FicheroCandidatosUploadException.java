package com.melilla.gestPlanes.exceptions.exceptions;

public class FicheroCandidatosUploadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FicheroCandidatosUploadException() {
		super("El formato de fichero no es válido.");
	}

}
