package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearPlantillaContratoDTO;
import com.melilla.gestPlanes.DTO.EditarPlantillaContratoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.PlantillaContratoConfigNotFoundException;
import com.melilla.gestPlanes.mappers.PlantillaContratoConfigMapper;
import com.melilla.gestPlanes.mappers.nominasReclamadasMapper;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;
import com.melilla.gestPlanes.repository.PlantillaContratoRepository;
import com.melilla.gestPlanes.service.PlanService;
import com.melilla.gestPlanes.service.PlantillaContratoConfigService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlantillaContratoConfigServiceImpl implements PlantillaContratoConfigService{

	@Autowired
	PlantillaContratoRepository plantillaContratoRepository;
	
	@Autowired
	PlanService planService;
	
	@Autowired
	private final PlantillaContratoConfigMapper plantillaMapper;
	
	@Override
	public List<PlantillaContratoConfig> obtenerPlantillas() {
		
		return plantillaContratoRepository.findAll();
	}

	@Override
	public PlantillaContratoConfig crearPlantilla(CrearPlantillaContratoDTO plantilla) {
		
		Plan planActivo = planService.getPlanActivo();
		
		PlantillaContratoConfig nuevaPlantilla = new PlantillaContratoConfig();
		
		nuevaPlantilla.setNombre(plantilla.getNombre());
		
		nuevaPlantilla.setPlan(planActivo);
		
		
		
		
		return plantillaContratoRepository.save(nuevaPlantilla);
	}

	@Override
	public PlantillaContratoConfig editarPlantilla(EditarPlantillaContratoDTO plantilla) {
		
		PlantillaContratoConfig plantillaBBDD = plantillaContratoRepository
				.findById(plantilla.getIdContratoConfig())
				.orElseThrow(()-> new PlantillaContratoConfigNotFoundException(plantilla.getIdContratoConfig()));
		
		plantillaBBDD = plantillaMapper.updatePlantillaContratoConfigFromEditarPlantillaContratoDTO(plantilla, plantillaBBDD);
		
		
		return plantillaContratoRepository.saveAndFlush(plantillaBBDD);
	}

	@Override
	public void borrarPlantilla(long idPlantilla) {
		
		plantillaContratoRepository.deleteById(idPlantilla);
		
	}

	@Override
	public PlantillaContratoConfig obtenerPlantilla(long idPlantilla) {
		
		return plantillaContratoRepository.findById(idPlantilla).orElseThrow(()-> new PlantillaContratoConfigNotFoundException(idPlantilla));
	}

}
