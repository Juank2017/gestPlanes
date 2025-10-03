package com.melilla.gestPlanes.DTO.importDTO;

import java.util.List;

import lombok.Data;

@Data
public class DataImport {

	private List<OrganismosImportDTO>organismos;
	
	private List<CategoriasImportDTO>categorias;
	
	private List<OcupacionesImportDTO>ocupaciones;
	
	private List<DatosPlanImportDTO>datosPlan;
	

	
}
