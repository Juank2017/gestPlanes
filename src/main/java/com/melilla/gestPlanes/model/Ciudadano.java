package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@Audited
@SQLDelete(sql = "UPDATE ciudadano SET deleted=true, deleted_at= NOW() WHERE id=?")
public class Ciudadano {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCiudadano;
	
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	private String sexo;
	
	@JsonProperty("DNI")
	private String DNI;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private String fechaNacimiento;
	
	private String estadoCivil;
	
	private String ccc;
	
	private String seguridadSocial;
	
	private String estado;
	
	@Email
	private String email;
	
	private String telefono;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaRegistro;
	
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
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
