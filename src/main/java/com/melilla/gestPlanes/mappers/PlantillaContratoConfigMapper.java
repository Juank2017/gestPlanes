package com.melilla.gestPlanes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.melilla.gestPlanes.DTO.EditarPlantillaContratoDTO;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;

@Mapper(componentModel = "spring")
public interface PlantillaContratoConfigMapper {
	
	PlantillaContratoConfigMapper MAPPER = Mappers.getMapper(PlantillaContratoConfigMapper.class);
	
	@Mapping(target="plan", ignore=true)
	PlantillaContratoConfig updatePlantillaContratoConfigFromEditarPlantillaContratoDTO(EditarPlantillaContratoDTO plantillaDTO, @MappingTarget PlantillaContratoConfig plantilla );

}
