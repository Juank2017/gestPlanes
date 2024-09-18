package com.melilla.gestPlanes.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idCiudadano")
@SQLDelete(sql = "UPDATE ciudadano SET deleted=true, deleted_at= NOW() WHERE id_ciudadano=?")
@EntityListeners(AuditingEntityListener.class)
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
	private LocalDate fechaNacimiento;
	
	private String estadoCivil;
	
	private String ccc;
	
	private String seguridadSocial;
	
	private String estado;
	
	private String numeroOrdenSepe;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaListadoSepe;
	
	@Email
	private String email;
	
	private String telefono;
	
	private String nacionalidad;
	
	boolean bajaLaboral;
	
	boolean bajaMaternal;
	
	boolean sinClausula;

	boolean antecedentes;
	boolean altaSS;
	boolean contrata;
	boolean escaneado;
	boolean nedaes;
	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaRegistro;
	
	@NotAudited
	@OneToMany(mappedBy = "ciudadano", cascade= CascadeType.ALL)
	private List<EmbargoCiudadano> embargos;
	
//	@JsonBackReference
//	@ManyToMany(mappedBy = "interesados")
//	private List<Expediente> expedientes;
	
	
	//@JsonBackReference
	@ManyToOne
	@JoinColumn(name="idEquipo")
	@NotAudited
	private Equipo equipo;
	
	@JsonManagedReference
	@OneToOne(mappedBy = "ciudadano")
	private Contrato contrato;
	
	@OneToMany(mappedBy="ciudadano",cascade=CascadeType.ALL)
	private List<ContratoReclamado>contratosReclamados;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "ciudadano", cascade = CascadeType.ALL)
	@NotAudited
	private List<NotaCiudadano> notas;
	
	@OneToOne
	@JoinColumn(name="idPlan")
	@NotAudited
	private Plan idPlan;
	
	@NotAudited
	@OneToMany(mappedBy = "ciudadano" , cascade= CascadeType.ALL)
	private List<Vacaciones> periodosVacaciones;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "ciudadano", cascade = CascadeType.ALL)
	private List<Documento> documentos= new ArrayList<Documento>();
	
	
//	@JsonBackReference
//	@OneToOne
//	@JoinColumn(name="idProcedimiento")
//	private Procedimiento procedimiento;
	
	
	
	private boolean esJefeEquipo;
	
	private boolean cupo;
	private boolean vg;
	
	private boolean reclamaSalarios;
	
	@CreatedDate
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	private LocalDateTime deletedAt;

}
