package com.melilla.gestPlanes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.melilla.gestPlanes.DTO.EditaNominaReclamadaDTO;
import com.melilla.gestPlanes.model.NominasReclamadas;

@Mapper(componentModel = "spring")
public interface nominasReclamadasMapper {
	
	nominasReclamadasMapper MAPPER = Mappers.getMapper(nominasReclamadasMapper.class);
	
	@Mapping(target= "contrato", ignore=true)
	NominasReclamadas editaNominaReclamadaDTOToNominasReclamadas(EditaNominaReclamadaDTO nomina);
	
	NominasReclamadas updateNominasReclamadasFromEditaNominaReclamadaDTO(EditaNominaReclamadaDTO nominaDTO, @MappingTarget NominasReclamadas nomina);

}
