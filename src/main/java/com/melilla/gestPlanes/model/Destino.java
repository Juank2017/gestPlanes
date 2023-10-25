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
public class Destino {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDestino;
	
	private String destino;
	
	@OneToOne
	@JoinColumn(name="idOrganismo")
	private Organismo idOrganismo;
	
	
	

}
