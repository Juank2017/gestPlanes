package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.EmbargoCiudadano;
import com.melilla.gestPlanes.model.Equipo;
import com.melilla.gestPlanes.model.NotaCiudadano;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.Vacaciones;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class listadoTrabajadoresDTO {

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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaInicio;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaFinal;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaExtincion;
	
	
	
	private String total;
	
	private boolean esJefeEquipo;
	
	private boolean cupo;
	private boolean vg;
	
	private String categoria;
	private String ocupacion;
	private String organismo;
	private String destino;
	private String telefono;
	
	private boolean deleted;
	
	private Long totalElements;
	
}
