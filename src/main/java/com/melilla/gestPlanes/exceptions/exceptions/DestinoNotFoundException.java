package com.melilla.gestPlanes.exceptions.exceptions;

public class DestinoNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DestinoNotFoundException(Long idDestino) {
		super("No se encuentra el destino con id: "+idDestino);
	}

}
