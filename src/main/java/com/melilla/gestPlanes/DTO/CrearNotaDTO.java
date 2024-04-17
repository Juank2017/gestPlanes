package com.melilla.gestPlanes.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CrearNotaDTO {
	
	
	private LocalDate fechaNota;
	private String asunto;
	private String nota;
	private boolean pinned;
	private Long idCiudadano;

}
