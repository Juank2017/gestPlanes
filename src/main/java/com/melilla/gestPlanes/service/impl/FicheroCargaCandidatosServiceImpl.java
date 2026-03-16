package com.melilla.gestPlanes.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.exceptions.exceptions.FicheroCandidatosUploadException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.model.FicheroCargaCandidatos;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.repository.FicheroCargaCandidatosRepository;
import com.melilla.gestPlanes.service.FicheroCargaCandidatosService;
import com.melilla.gestPlanes.service.PlanConfigService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;

@Log
@Service
public class FicheroCargaCandidatosServiceImpl implements FicheroCargaCandidatosService {

	@Autowired
	PlanConfigService planConfigService;

	@Autowired
	PlanService planService;

	@Autowired
	FicheroCargaCandidatosRepository ficheroCargaCandidatosRepository;

	@Override
	public FicheroCargaCandidatos subirFichero(MultipartFile fichero) {

		if (!fichero.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {

			throw new FicheroCandidatosUploadException();
		}

		FicheroCargaCandidatos ficheroCargaCandidatos = new FicheroCargaCandidatos();

		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir()).toAbsolutePath().normalize();

		try {

			Files.createDirectories(fileStorageLocation);

			// nombre del fichero
			String fileName = StringUtils.cleanPath(fichero.getOriginalFilename());

			String contentType = fichero.getContentType();

			log.warning(contentType);

			if (fileName.contains("..")) {
				throw new FileStorageException(
						"El nombre de archivo tiene una secuencia de carácteres no válida " + fileName);
			}
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(fichero.getInputStream(), targetLocation);

			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(config.getUploadTemplateDir()+"/").path(fileName).toUriString();

			ficheroCargaCandidatos.setIdPlan(planService.getWorikingPlan());
			ficheroCargaCandidatos.setFileName(fileName);
			ficheroCargaCandidatos.setProcesado(false);
			ficheroCargaCandidatos.setURL(fileDownladUri);

			ficheroCargaCandidatos = ficheroCargaCandidatosRepository.save(ficheroCargaCandidatos);

		} catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo " + fichero + " ya existe");
		} catch (Exception IOException) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		return ficheroCargaCandidatos;
	}

	@Override
	public void borrarFichero(long idFichero) {
		FicheroCargaCandidatos fichero = ficheroCargaCandidatosRepository.findById(idFichero)
				.orElseThrow(() -> new MyFileNotFoundException("Fichero no encontrado"));

		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "\\" + fichero.getFileName())
				.toAbsolutePath().normalize();
		try {

			Files.delete(fileStorageLocation);
			ficheroCargaCandidatosRepository.delete(fichero);

		} catch (IOException e) {
			throw new FileStorageException("No se ha podido borrar el fichero: " + fileStorageLocation);
		}

	}

	@Override
	public List<FicheroCargaCandidatos> obtenerListadoFicheros() {
		
		Plan plan = planService.getWorikingPlan();

		return ficheroCargaCandidatosRepository.findAll().stream().filter((f) -> f.isDeleted() == false && f.getIdPlan().getIdPlan() == plan.getIdPlan()).toList();
	}

	@Override
	public Resource descargarFichero(long id) {
		FicheroCargaCandidatos fichero = ficheroCargaCandidatosRepository.findById(id)
				.orElseThrow(() -> new MyFileNotFoundException("Fichero no encontrado"));
		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "\\" + fichero.getFileName())
				.toAbsolutePath().normalize();
		try {
			Resource resource = new UrlResource(fileStorageLocation.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fichero.getFileName());
			}
		} catch (MalformedURLException e) {
			throw new MyFileNotFoundException("File not found " + fichero.getFileName());
		}

	}

	@Override
	public List<FicheroCargaCandidatos> procesaFichero(long idFichero) {
		
		return null;
	}

}
