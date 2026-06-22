package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;


import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;


import lombok.Data;

@Data
public class TrabajadorResponseDTO {
	
private	long idCiudadano;
private	String nombre;
private	String apellido1;
private	String apellido2;
private	String sexo;
private	String DNI;
private	String seguridadSocial;
private	LocalDate fechaNacimiento;
private	 String ccc;
private	 String estado;
private	String email;
private	 String telefono;
private	 LocalDate fechaRegistro;
private	String gc;
private	String grupoConvenio;
private	  boolean bajaLaboral;
private	 boolean bajaMaternal;
private	long categoria;
private	long ocupacion;
private	int numeroOrdenSepe;
private	 LocalDate fechaInicio;
private	LocalDate  fechaFinal;
private	  LocalDate fechaExtincion;
private	 int duracion;
private	  String turno;
private	 long entidad;
private	 long destino;
private	  String base;
private String prorratas;
private String residencia;
private String total;
private	 long equipo;
private	   String nacionalidad;
private	 boolean sinClausula;
private	 boolean   formacion;
private	  boolean   evaluacion;
private	  boolean   reconocimiento;
private	 boolean deleted;
private	 LocalDate fechaListadoSepe;
private	  boolean suplente;
private boolean esJefeEquipo;


   
   public static TrabajadorResponseDTO ciudadanoToTrabajadorResponseDTO (Ciudadano ciudadano) {
	   
	   if(ciudadano == null ) return null;
	   
	   TrabajadorResponseDTO trabajador = new TrabajadorResponseDTO();
	   
	   
	   
	   trabajador.setIdCiudadano(ciudadano.getIdCiudadano());
	   trabajador.setFechaRegistro(ciudadano.getFechaRegistro());
	   trabajador.setEstado(ciudadano.getEstado());
	   trabajador.setNombre(ciudadano.getNombre());
	   trabajador.setApellido1(ciudadano.getApellido1());
	   trabajador.setApellido2(ciudadano.getApellido2());
	   trabajador.setDNI(ciudadano.getDNI());
	   trabajador.setSexo(ciudadano.getSexo());
	   trabajador.setEmail(ciudadano.getEmail());
	   trabajador.setCcc(ciudadano.getCcc());
	   trabajador.setGc(ciudadano.getEstado());
	   trabajador.setSeguridadSocial(ciudadano.getSeguridadSocial());
	   trabajador.setNacionalidad(ciudadano.getNacionalidad());
	   trabajador.setTelefono(ciudadano.getTelefono());
	   trabajador.setFechaNacimiento(ciudadano.getFechaNacimiento());
	   trabajador.setFormacion(ciudadano.isFormacion());
	   trabajador.setReconocimiento(ciudadano.isReconocimiento());
	   trabajador.setBajaLaboral(ciudadano.isBajaLaboral());
	   trabajador.setBajaMaternal(ciudadano.isBajaMaternal());
	   trabajador.setEsJefeEquipo(ciudadano.isEsJefeEquipo());
	   
	   if(ciudadano.getEquipo() != null) trabajador.setEquipo(ciudadano.getEquipo().getIdEquipo());
	  
	   
	   
	   Contrato contrato = ciudadano.getContrato();
	   if (contrato != null) {
		   trabajador.setGc(contrato.getGc());
		   
		   if (contrato.getCategoria() != null) trabajador.setCategoria(contrato.getCategoria().getIdCategoria());
		
		   if (contrato.getOcupacion() != null) trabajador.setOcupacion(contrato.getOcupacion().getIdOcupacion());
		
		   if (contrato.getEntidad() != null) trabajador.setEntidad(contrato.getEntidad().getIdOrganismo()); 
		   
		   if (contrato.getDestino() != null) trabajador.setDestino(contrato.getDestino().getIdDestino());
		   
		   trabajador.setBase(contrato.getBase());
		   
		   trabajador.setProrratas(contrato.getProrratas());
		   
		   trabajador.setResidencia(contrato.getResidencia());
		   
		   trabajador.setTotal(contrato.getTotal());
		   
		   trabajador.setDuracion(contrato.getDuracion());
		   
		   trabajador.setFechaInicio(contrato.getFechaInicio());
		   
		   trabajador.setFechaFinal(contrato.getFechaFinal());
		   
		   trabajador.setFechaExtincion(contrato.getFechaExtincion());
		   
		   trabajador.setTurno(contrato.getTurno());
		   
		   
	   
	   }
	   
	   
	   
	   return trabajador;
   }

}
