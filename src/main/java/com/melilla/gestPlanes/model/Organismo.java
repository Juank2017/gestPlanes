package com.melilla.gestPlanes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Organismo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOrganismo;
	
	private String organismo;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	private Plan idPlan;
	
}
