package com.melilla.gestPlanes.exceptions.exceptions;

public class DataStaleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataStaleException() {
		super("Los datos están obsoletos.");
	}

}
