package com.melilla.gestPlanes.exceptions.exceptions;

public class DocumentoNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DocumentoNotFoundException(Long idDocumento) {
		super("No se encuentra el documento con id: "+idDocumento);
	}

}
