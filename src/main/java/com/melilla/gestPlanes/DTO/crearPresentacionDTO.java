package com.melilla.gestPlanes.DTO;

import lombok.Data;

/**
 * DTO para recibir los datos de una presentación desde el front.
 */
@Data
public class crearPresentacionDTO {
	
	private String presentacion;
	private String responsable;
	private String vacaciones;
	private String observaciones;

}
