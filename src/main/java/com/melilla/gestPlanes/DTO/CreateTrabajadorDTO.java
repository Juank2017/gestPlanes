package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un trabajador.
 */
@Data
@Builder
public class CreateTrabajadorDTO {
//TODO: revisar la eliminación de los campos booleanos que ya no se usan.
	
	String nombre;
	String apellido1;
	String apellido2;
	String sexo;
	String DNI;
	@Builder.Default
	String seguridadSocial = "00/00000000/00";
	LocalDate fechaNacimiento;
	@Builder.Default
	String ccc = "";
	long idPlan;
	String estado;
	//Orden en el listado del SEPE
	int numeroOrdenSepe;
	String email;
	String telefono;
	LocalDate fechaRegistro;
	Long gc;
	Long categoria;
	Long ocu;
	//duración del contrato
	int duracion;
	LocalDate fechaInicio;
	LocalDate fechaFinal;
	String turno;
	//El organismo
	Long entidad;
	Long destino;
	String base;
	String prorratas;
	String residencia;
	String total;
	Long equipo;
	String nacionalidad;
	//sin periodo de prueba
	boolean sinClausula;
	boolean antecedentes;
	boolean altaSS;
	boolean contrata;
	boolean escaneado;
	boolean nedaes;
	LocalDate fechaListadoSepe;
	boolean reclamaSalarios;
	boolean suplente;

}
