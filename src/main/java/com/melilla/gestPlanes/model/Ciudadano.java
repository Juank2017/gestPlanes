package com.melilla.gestPlanes.model;

import java.util.List;


import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


import com.melilla.gestPlanes.model.enums.EstadoCiudadano;
import com.melilla.gestPlanes.model.enums.EstadoCivil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
@Audited
public class Ciudadano {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCiudadano;
	
	private String nombre;
	
	private String apellidos;
	
	private String DNI;
	
	private String fechaNacimiento;
	
	private EstadoCivil estadoCivil;
	
	private String ccc;
	
	private String seguridadSocial;
	
	private EstadoCiudadano estado;
	
	private String email;
	
	private String telefono;
	
	@NotAudited
	@OneToMany(mappedBy = "ciudadano", cascade= CascadeType.ALL)
	private List<EmbargoCiudadano> embargos;
	
	@ManyToMany(mappedBy = "interesados")
	private List<Expediente> expedientes;
	
	@ManyToOne
	@JoinColumn(name="idEquipo")
	@NotAudited
	private Equipo equipo;
	
	

}
