package com.melilla.gestPlanes.exceptions.exceptions;

public class ExcelParseErrorException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ExcelParseErrorException(int i,int col,String msg) {
		super ("Error al leer la línea "+i+" columna "+col+". "+msg);
	}

}
