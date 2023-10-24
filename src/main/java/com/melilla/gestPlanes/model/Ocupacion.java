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
public class Ocupacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idOcupacion;
	
	private String ocupacion;
	
	private String ocupacionSEPE;
	
	@OneToOne
	@JoinColumn(name="idCategoria")
	private Categoria categoria;

}
