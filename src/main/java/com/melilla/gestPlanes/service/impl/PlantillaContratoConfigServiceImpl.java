package com.melilla.gestPlanes.service.impl;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.DTO.CrearPlantillaContratoDTO;
import com.melilla.gestPlanes.DTO.EditarPlantillaContratoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.PlantillaContratoConfigNotFoundException;
import com.melilla.gestPlanes.mappers.PlantillaContratoConfigMapper;

import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;
import com.melilla.gestPlanes.repository.PlantillaContratoRepository;
import com.melilla.gestPlanes.service.PlanConfigService;
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
	PlanConfigService planConfigService;
	
	@Autowired
	private final PlantillaContratoConfigMapper plantillaMapper;
	
	@Override
	public List<PlantillaContratoConfig> obtenerPlantillas() {
		
		Plan planActivo = planService.getWorikingPlan();
		
		return plantillaContratoRepository.findAllByPlan(planActivo);
	}

	@Override
	public PlantillaContratoConfig crearPlantilla(CrearPlantillaContratoDTO plantilla) {
		
		Plan planActivo = planService.getWorikingPlan();
		
		PlantillaContratoConfig nuevaPlantilla = new PlantillaContratoConfig();
		
		nuevaPlantilla.setNombre(plantilla.getNombre());
		
		nuevaPlantilla.setPlan(planActivo);
		
		nuevaPlantilla.setActiva(false);
		
		nuevaPlantilla =plantillaContratoRepository.save(nuevaPlantilla);
		
		activarPlantilla(nuevaPlantilla.getIdContratoConfig());
		
		return nuevaPlantilla;
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

	@Override
	public PlantillaContratoConfig activarPlantilla(long idPlantilla) {
		
		List<PlantillaContratoConfig>plantillas = obtenerPlantillas();
		
		plantillas.forEach((p)->p.setActiva(false));
		
		PlantillaContratoConfig plantilla = plantillaContratoRepository.findById(idPlantilla).orElseThrow(()-> new PlantillaContratoConfigNotFoundException(idPlantilla));
		
		plantilla.setActiva(true);
		
		plantillaContratoRepository.save(plantilla);
		
		return plantilla;
	}

	@Override
	public PlantillaContratoConfig obtenerPlantillaActiva(Plan plan) {
		
		return plantillaContratoRepository.findByPlanAndActiva(plan, true);
	}

	@Override
	public PlantillaContratoConfig subirPlantilla(MultipartFile file) {
		PlanConfig config = planConfigService.obtenerConfig(planService.getPlanActivo().getIdPlan());
		
		Path fileStorageLocation = Paths.get(config.getTemplateDir() ).toAbsolutePath().normalize();
		PlantillaContratoConfig plantillaContratoConfig = obtenerPlantillaActiva(planService.getPlanActivo());
		
		if (plantillaContratoConfig == null) throw new PlantillaContratoConfigNotFoundException(0); 
		
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		// nombre del fichero
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// fileName = tipo + "_" + fileName;
		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException(
						"El nombre de archivo tiene una secuencia de carácteres no válida " + fileName);
			}
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation);

			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/plantilla/")
					.path(fileName).toUriString();
			
			
			
			plantillaContratoConfig.setNombreFicheroPlantilla(fileName);
			plantillaContratoConfig.setUrl(fileDownladUri);
			
			plantillaContratoRepository.save(plantillaContratoConfig);

		} catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo " + fileName + " ya existe");
		} catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}
		
		return plantillaContratoConfig;
	}

}
