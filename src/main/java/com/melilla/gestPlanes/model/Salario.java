package com.melilla.gestPlanes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Salario {

	@Id
	private int grupo;
	
	private String base;
	
	private String prorrata;
	
	private String residencia;
	
	private String total;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	private Plan plan;
}
