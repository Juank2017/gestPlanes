package com.melilla.gestPlanes.model;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Embeddable
@Data
public class OrganismoOcupacionId implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	private Organismo organismo;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Ocupacion ocupacion;
	

	
	

}
