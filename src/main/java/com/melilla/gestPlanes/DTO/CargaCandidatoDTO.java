package com.melilla.gestPlanes.DTO;



import java.util.Date;

import lombok.Data;

@Data
public class CargaCandidatoDTO {
	
	private String ordenSEPE;
	
	private Date fechaListadoSEPE;
	
	private String nombre;
	
	private String apellido1;
	
	private String apellido2;
	
	private String telefono;
	
	private String dni;
	
	private String email;

}
