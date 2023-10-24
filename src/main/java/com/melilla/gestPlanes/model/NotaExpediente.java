package com.melilla.gestPlanes.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class NotaExpediente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idNota;
	
	@Temporal(TemporalType.DATE)
	private Date fechaNota;
	
	private String nota;
	
	private boolean pinned;
	
	@ManyToOne
	@JoinColumn(name="idExpediente")
	private Expediente expediente;

}
