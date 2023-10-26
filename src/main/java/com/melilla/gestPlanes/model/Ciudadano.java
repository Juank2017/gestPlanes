package com.melilla.gestPlanes.model;

import java.util.Date;
import java.util.List;


import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@Entity
@Audited
public class Ciudadano {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCiudadano;
	
	private String nombre;
	
	private String apellidos;
	
	private String sexo;
	
	private String DNI;
	
	private String fechaNacimiento;
	
	private String estadoCivil;
	
	private String ccc;
	
	private String seguridadSocial;
	
	private String estado;
	
	@Email
	private String email;
	
	private String telefono;
	
	@Temporal(TemporalType.DATE)
	private Date fechaRegistro;
	
	@NotAudited
	@OneToMany(mappedBy = "ciudadano", cascade= CascadeType.ALL)
	private List<EmbargoCiudadano> embargos;
	
//	@JsonBackReference
//	@ManyToMany(mappedBy = "interesados")
//	private List<Expediente> expedientes;
	
	@ManyToOne
	@JoinColumn(name="idEquipo")
	@NotAudited
	private Equipo equipo;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "ciudadano")
	private Contrato contrato;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "ciudadano", cascade = CascadeType.ALL)
	@NotAudited
	private List<NotaCiudadano> notas;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	@NotAudited
	private Plan idPlan;
	
	@OneToMany(mappedBy = "ciudadano", cascade = CascadeType.ALL)
	private List<Documento> documentos;
	
	private boolean esJefeEquipo;

}
