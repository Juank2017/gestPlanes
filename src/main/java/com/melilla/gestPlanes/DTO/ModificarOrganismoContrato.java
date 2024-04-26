package com.melilla.gestPlanes.DTO;

import lombok.Data;

@Data
public class ModificarOrganismoContrato {

	private String dni;
	private Long idCiudadano;
	private Long organismo;
	private Long destino;
}
