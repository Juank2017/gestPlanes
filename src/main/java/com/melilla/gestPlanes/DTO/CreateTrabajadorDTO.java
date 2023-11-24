package com.melilla.gestPlanes.DTO;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateTrabajadorDTO {

	String nombre;
	String apellido1;
	String apellido2;
	String sexo;
	String DNI;
	String seguridadSocial;
	String fechaNacimiento;
	String ccc;
	String estado;
	String email;
	String telefono;
	Date fechaRegistro;
	Long gc;
	Long categoria;
	Long ocu;
	String duracion;
	Date fechaInicio;
	Date fechaFinal;
	String turno;
	Long entidad;
	Long destino;
	String base;
	String prorratas;
	String residencia;
	String total;

}
