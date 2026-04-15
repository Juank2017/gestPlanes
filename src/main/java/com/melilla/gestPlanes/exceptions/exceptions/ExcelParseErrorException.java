package com.melilla.gestPlanes.exceptions.exceptions;

public class ExcelParseErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ExcelParseErrorException(int i,String msg) {
		super ("Error al leer la línea "+i+" "+msg);
	}

}
