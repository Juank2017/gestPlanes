package com.melilla.gestPlanes.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class EmbargoCiudadano {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long IdEmbargo;
	
	private Long cantidad;
	
	private String organismo;
	
	
	@ManyToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	private String tipo;
	
}
