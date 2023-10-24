package com.melilla.gestPlanes.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Vacaciones {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idVacaciones;
	
	private Date FechaInicio;
	
	private Date FechaFin;
	
	private int dias;
	
	@ManyToOne
	@JoinColumn(name="idContrato")
	private Contrato contrato;
	
	
}
