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
public class Proyecto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProyecto;
	
	private String proyecto;
	
	@OneToOne
	@JoinColumn(name = "idOrganismo")
	private Organismo idOrganismo;

}
