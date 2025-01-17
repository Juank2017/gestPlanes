package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearPlantillaContratoDTO;
import com.melilla.gestPlanes.DTO.EditarPlantillaContratoDTO;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;

public interface PlantillaContratoConfigService {
	
	List<PlantillaContratoConfig> obtenerPlantillas();
	
	PlantillaContratoConfig obtenerPlantilla(long idPlantilla);
	
	PlantillaContratoConfig crearPlantilla(CrearPlantillaContratoDTO plantilla);
	
	PlantillaContratoConfig editarPlantilla(EditarPlantillaContratoDTO plantilla);
	
	void borrarPlantilla(long idPlantilla);
	
	

}
