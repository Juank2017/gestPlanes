package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

/**
 * DTO para recibir los datos de una nota.
 */
@Data
public class CrearNotaDTO {

	private LocalDate fechaNota;
	private String asunto;
	private String nota;
	// hace que la nota se quede la primera en el listado.
	private boolean pinned;
	private Long idCiudadano;

}
