package com.melilla.gestPlanes.exceptions.exceptions;

public class OrganismoNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public OrganismoNotFoundException(Long idOrganismo) {
		super("No se encuentra el organismo con id "+idOrganismo);
	}

}
