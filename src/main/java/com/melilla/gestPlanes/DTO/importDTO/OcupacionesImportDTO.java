package com.melilla.gestPlanes.DTO.importDTO;

import lombok.Data;

@Data
public class OcupacionesImportDTO {
	
	private long idOcupacion;
	
	private String codigoSepe;
	
	private String ocupacion;
	
	private long categoria_idcategoria;
	
	private long idPlan;
	
}


