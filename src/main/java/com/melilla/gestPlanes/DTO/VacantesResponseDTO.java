package com.melilla.gestPlanes.DTO;

import lombok.Data;

@Data
public class VacantesResponseDTO {
	private Long id;
	private String organismo;
	private String ocupacion;
	private int previstos;
	private int contratados;
	private int parciales;
	private int vacantes;

}
