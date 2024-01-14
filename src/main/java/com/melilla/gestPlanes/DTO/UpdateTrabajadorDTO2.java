package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateTrabajadorDTO2 {

	String nombre;
	String apellido1;
	String apellido2;
	String sexo;
	String DNI;
	String seguridadSocial;
	LocalDate fechaNacimiento;
	String ccc;
	String estado;
	String email;
	String telefono;
	LocalDate fechaRegistro;
	Long gc;
	Long categoria;
	Long ocu;
	int duracion;
	LocalDate fechaInicio;
	LocalDate fechaFinal;
	LocalDate fechaExtincion;
	String turno;
	Long entidad;
	Long destino;
	String base;
	String prorratas;
	String residencia;
	String total;

}
