package com.melilla.gestPlanes.exceptions.exceptions;

public class ProcedimientoNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public ProcedimientoNotFoundException(Long idContrato) {
		super("No se encuentra el contrato reclamado: "+ idContrato);
	}

}
