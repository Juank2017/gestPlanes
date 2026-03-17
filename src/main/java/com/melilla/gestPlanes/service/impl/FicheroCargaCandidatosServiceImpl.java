package com.melilla.gestPlanes.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.DTO.CargaCandidatoDTO;
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
import com.melilla.gestPlanes.util.DNIValidator;

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
		
		 FicheroCargaCandidatos fichero = ficheroCargaCandidatosRepository.findById(idFichero)
				.orElseThrow(() -> new MyFileNotFoundException("Fichero no encontrado"));
		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "\\" + fichero.getFileName())
				.toAbsolutePath().normalize();
		
		List<CargaCandidatoDTO> candidatos =excelToCargaCandidatoDTOList(fileStorageLocation);
		
		
		
		return null;
	}

	private List<CargaCandidatoDTO> excelToCargaCandidatoDTOList(Path fileStorageLocation) {
		Workbook wb;
		List<CargaCandidatoDTO> candidatos =new  ArrayList<CargaCandidatoDTO>();
		try {
			Resource resource = new UrlResource(fileStorageLocation.toUri());
			
			InputStream inp = new FileInputStream(resource.getFile());
		    wb = WorkbookFactory.create(inp);
		    Sheet sheet= wb.getSheetAt(0);
		    
		    int lastRow = sheet.getLastRowNum()+1;
		    
		    
		    
		    for (int i = 1; i < lastRow; i++) {
				
		    	 Row row = sheet.getRow(i);
		    	 
		    	 CargaCandidatoDTO candidato = new CargaCandidatoDTO();
		    	 
		    	 candidato.setOrdenSEPE(row.getCell(0).getStringCellValue());
		    	 candidato.setFechaListadoSEPE(row.getCell(1).getDateCellValue());
		    	 candidato.setDni(row.getCell(2).getStringCellValue());
		    	 candidato.setNombre(row.getCell(3).getStringCellValue());
		    	 candidato.setApellido1(row.getCell(4).getStringCellValue());
		    	 candidato.setApellido2(row.getCell(5).getStringCellValue());
		    	 candidato.setTelefono(row.getCell(6).getStringCellValue()+ "/" +row.getCell(7).getStringCellValue());
		    	 candidato.setEmail(row.getCell(8).getStringCellValue());
		    	 
		    	 candidatos.add(candidato);
		    
		    	 
		    	 
			}
		    wb.close();
		  
		   
			
		} catch (MalformedURLException e) {
			throw new MyFileNotFoundException("No se encuentra el fichero excel");
		}catch(FileNotFoundException e) {
			throw new MyFileNotFoundException("No se encuentra el fichero excel");
		}
		catch(IOException e) {
			
		}
		return candidatos;
	}

}
