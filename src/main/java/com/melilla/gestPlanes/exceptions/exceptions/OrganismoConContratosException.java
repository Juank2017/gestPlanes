package com.melilla.gestPlanes.exceptions.exceptions;

public class OrganismoConContratosException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public OrganismoConContratosException(long idOrganismo) {
		super("El organismo con id "+ idOrganismo+" tiene contratos y no puede ser borrado.");
	}
	

}
