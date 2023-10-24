package com.melilla.gestPlanes.model;

import java.util.Date;
import java.util.List;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
@Audited
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContrato;
	
	private Date fechaInicio;
	
	private Date fechaFinal;
	
	private Date fechaExtincion;
	
	private String ocupacion;
	
	private String categoria;
	
	private String gc;
	
	private String entidad;
	
	private String destino;
	
	private int duracion;
	
	private String porcentajeHoras;
	
	private String base;
	
	private String prorratas;
	
	private String residencia;
	
	private String total;
	
	private int diasVacaciones;
	
	@NotAudited
	@OneToMany(mappedBy = "contrato" , cascade= CascadeType.ALL)
	private List<Vacaciones> periodosVacaciones;
	
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	

}
