package com.melilla.gestPlanes.model;

import java.util.Date;
import java.util.List;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Audited
public class Contrato {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idContrato;
	
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaFinal;
	
	@Temporal(TemporalType.DATE)
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
	
	private String turno;
	
	private int diasVacaciones;
	
	@NotAudited
	@OneToMany(mappedBy = "contrato" , cascade= CascadeType.ALL)
	private List<Vacaciones> periodosVacaciones;
	
	@JsonBackReference
	@OneToOne
	@JoinColumn(name="idCiudadano")
	private Ciudadano ciudadano;
	
	

}
