package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateProcedimientoDTO {
	
	
	private String numeroProcedimiento;
	
	private String sentencia;
	
	private LocalDate fechaSentencia;
	
	private String nombreTrabajador;
	
	private String apellido1Trabajador;
	
	private String apellido2Trabajador;
	
	private String dni;
	
	private String ssTrabajador;
	
	private long representante;
	
	private List<CreatePeriodosReclamadosDTO>periodos;
	
	private boolean reclamaSalarios = true;

}
