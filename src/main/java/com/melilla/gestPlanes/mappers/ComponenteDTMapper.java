package com.melilla.gestPlanes.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.melilla.gestPlanes.DTO.ComponentesEquipoDTO;
import com.melilla.gestPlanes.model.Ciudadano;

@Mapper(componentModel = "spring")
public interface ComponenteDTMapper {
	
	ComponenteDTMapper MAPPER = Mappers.getMapper(ComponenteDTMapper.class);
	
	
	
	@Mapping(target="idCiudadano",source="ciudadano.idCiudadano")
	@Mapping(target="jefe",source="ciudadano.esJefeEquipo")
	@Mapping(target="nombre",source="ciudadano.nombre")
	@Mapping(target="apellido1",source="ciudadano.apellido1")
	@Mapping(target="apellido2",source="ciudadano.apellido2")
	@Mapping(target="telefono",source="ciudadano.telefono")
	@Mapping(target="dni",source="ciudadano.DNI")
	@Mapping(target="turno",source="ciudadano.contrato.turno")
	@Mapping(target="ocupacion",source="ciudadano.contrato.ocupacion.ocupacion")
	@Mapping(target="organismo",source="ciudadano.contrato.entidad.nombreCortoOrganismo")
	@Mapping(target="destino",source="ciudadano.contrato.destino.destino")
	ComponentesEquipoDTO ciudadanoToComponentesEquipoDTO(Ciudadano ciudadano,ComponentesEquipoDTO componentes);

}
