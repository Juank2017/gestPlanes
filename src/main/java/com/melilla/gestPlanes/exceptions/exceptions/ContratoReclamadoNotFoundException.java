package com.melilla.gestPlanes.exceptions.exceptions;

public class ContratoReclamadoNotFoundException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ContratoReclamadoNotFoundException(Long idContrato) {
		super("No se encuentra el contrato reclamado: "+ idContrato);
	}

}
