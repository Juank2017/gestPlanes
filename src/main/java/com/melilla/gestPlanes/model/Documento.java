package com.melilla.gestPlanes.model;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Audited
public class Documento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idDocumento;
	
	private String ruta;
	
	private String nombre;
	
	private String tipo;
	
	private String observaciones;
	
	@ManyToOne
	@JoinColumn(name="idExpediente")
	private Expediente expediente;
	

}
