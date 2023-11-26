package com.melilla.gestPlanes.exceptions.exceptions;

public class CategoriaNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CategoriaNotFoundException(Long idCategoria) {
		super("No se encuentra la categoria con id: "+idCategoria);
	}
	
	

}
