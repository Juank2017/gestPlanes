package com.melilla.gestPlanes.DTO;

import lombok.Data;

@Data
public class VacantesResponseDTO {
	private Long id;
	private Long idOrganismo;
	private String organismo;
	private Long idOcupacion;
	private String ocupacion;
	private int previstos;
	private int contratados;
	private int parciales;
	private int vacantes;

}
