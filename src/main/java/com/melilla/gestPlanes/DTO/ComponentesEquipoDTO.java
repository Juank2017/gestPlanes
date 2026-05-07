package com.melilla.gestPlanes.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComponentesEquipoDTO {

	private String dni;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String telefono;
	private String turno;
	private String ocupacion;
	private String organismo;
	private String destino;
	private long idCiudadano;
	private boolean jefe;
}
