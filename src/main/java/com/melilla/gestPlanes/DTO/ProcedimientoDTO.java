package com.melilla.gestPlanes.DTO;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ProcedimientoDTO {
	
	private Long idProcedimiento;
	
	private String numeroProcedimiento;
	
	private String sentencia;
	
	
	
	private Long idAbogado;
	
	private String nombreAbogado;
	
	private String apellido1Abogado;
	
	private String apellido2Abogado;

	private String numeroColegiado;
	
	private String telefono;
	
	private String email;
	
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	private String DNI;
	
	private String seguridadSocial;
	
	private BigDecimal  totalDevengado;
	
	private BigDecimal totalPercibido;
	
	private BigDecimal totalReclamado;
	
	private BigDecimal totalReconocido;
	
	private BigDecimal totalAbonado;
	
	
	
}
