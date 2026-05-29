package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class TrabajadoresDTO {
private Long idCiudadano;
	
	private long idPlan;
	
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
	
	private int numeroOrdenSepe;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_ES" )
	private LocalDate fechaListadoSepe;
	
	@Email
	private String email;
	
	
	
	private String nacionalidad;
	
	boolean bajaLaboral;
	
	boolean bajaMaternal;
	
	boolean sinClausula;

	boolean formacion;
	boolean evaluacion;
	boolean reconocimiento;
	boolean escaneado;
	boolean nedaes;
	boolean suplente;
	
	
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
	
	private int gc;
	private String categoria;
	private String ocupacion;
	private String organismo;
	private String destino;
	private String telefono;
	private String turno;
	
	private boolean deleted;
	
	private boolean notas;
	
	private String equipo;
	
	private int vacacionesDisfrutadas;
}
