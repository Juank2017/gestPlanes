package com.melilla.gestPlanes.DTO;

import java.util.List;

import com.melilla.gestPlanes.model.ContratoReclamado;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CiudadanoReclamacionDTO {

	private Long idCiudadano;
	
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	private String DNI;
	
	private String seguridadSocial;
	
	private List<ContratoReclamado>contratos;
}
