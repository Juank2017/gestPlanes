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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.FicheroCargaCandidatosResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.ExcelParseErrorException;
import com.melilla.gestPlanes.exceptions.exceptions.FicheroCandidatosUploadException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.FicheroCargaCandidatos;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.FicheroCargaCandidatosRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.FicheroCargaCandidatosService;
import com.melilla.gestPlanes.service.OcupacionService;
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

	@Autowired
	OcupacionRepository ocupacionRepository;

	@Autowired
	CiudadanoService ciudadanoService;

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
					.path(config.getUploadTemplateDir() + "/").path(fileName).toUriString();

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

		return ficheroCargaCandidatosRepository.findAll().stream()
				.filter((f) -> f.isDeleted() == false && f.getIdPlan().getIdPlan() == plan.getIdPlan()).toList();
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
	public FicheroCargaCandidatosResponseDTO procesaFichero(long idFichero) {

		FicheroCargaCandidatosResponseDTO response = new FicheroCargaCandidatosResponseDTO();

		FicheroCargaCandidatos fichero = ficheroCargaCandidatosRepository.findById(idFichero)
				.orElseThrow(() -> new MyFileNotFoundException("Fichero no encontrado"));
		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "\\" + fichero.getFileName())
				.toAbsolutePath().normalize();

		List<Ciudadano> creados = new ArrayList<Ciudadano>();

		List<CreateTrabajadorDTO> candidatos = excelToCargaCandidatoDTOList(fileStorageLocation);

		List<CreateTrabajadorDTO> candidatosConError = new ArrayList<CreateTrabajadorDTO>();

		Iterator<CreateTrabajadorDTO> it = candidatos.iterator();

		while (it.hasNext()) {

			CreateTrabajadorDTO candidato = it.next();

			String dni = candidato.getDNI();

			try {
				if (DNIValidator.validate(dni)) {
					log.info("dni ok");

					Ciudadano ciudadano = ciudadanoService.crearTrabajador(candidato);

					creados.add(ciudadano);

				}
			} catch (OcupacionNotFoundException e) {
				log.info("OcupacionNotFound");
				candidatosConError.add(candidato);
				e.printStackTrace();

			} catch (Exception e) {

				candidatosConError.add(candidato);
				e.printStackTrace();

			}

		}

		response.setCandidatos(creados);
		response.setCandidatosConError(candidatosConError);

		return response;
	}

	private List<CreateTrabajadorDTO> excelToCargaCandidatoDTOList(Path fileStorageLocation) {
		Workbook wb;
		int i = 0;
		int j = 0;
		int col = 0;
		List<CreateTrabajadorDTO> candidatos = new ArrayList<CreateTrabajadorDTO>();
		long idPlan = planService.getWorikingPlan().getIdPlan();
		try {

			Resource resource = new UrlResource(fileStorageLocation.toUri());

			InputStream inp = new FileInputStream(resource.getFile());
			wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);

			int lastRow = sheet.getLastRowNum();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			log.info(lastRow + "");
			LocalDate fechaOrigen = LocalDate.of(1899, 12, 31);
			
			for (i = 1; i <= lastRow; i++) {

				Row row = sheet.getRow(i);
				CreateTrabajadorDTO candi = new CreateTrabajadorDTO();
				
				for ( j = 0;i<=13;j++) {
					
					switch (j) {
					case 0: {
						if(row.getCell(0) != null) {
							candi.setNumeroOrdenSepe((int) row.getCell(0).getNumericCellValue());
						}else {
							throw new ExcelParseErrorException(i, j, "hay un valor null en la celda o está vacía.");
						}
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + j);
					}
				}

				//CreateTrabajadorDTO candi = new CreateTrabajadorDTO();
				log.info(i + "");
				if (row.getCell(0) == null || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null
						|| row.getCell(6) == null || row.getCell(4) == null || row.getCell(12) == null
						|| row.getCell(10) == null) {
					throw new ExcelParseErrorException(i, col, "hay un valor null en la celda o está vacía.");
				} else {
					Long idOcupacion = (long) row.getCell(12).getNumericCellValue();
					col = 12;
					Ocupacion ocupacion = ocupacionRepository.findById(idOcupacion)
							.orElseThrow(() -> new OcupacionNotFoundException(idOcupacion));

					long idCategoria = ocupacion.getCategoria().getIdCategoria();

					long gc = (long) ocupacion.getCategoria().getGrupo();
					col = 0;
					candi.setNumeroOrdenSepe((int) row.getCell(0).getNumericCellValue());
					col = 1;
					candi.setFechaListadoSepe(fechaOrigen.plusDays((long) row.getCell(1).getNumericCellValue() - 1));
					col = 2;
					candi.setSuplente(row.getCell(2).getStringCellValue().equals("NO") ? false : true);
					col = 3;
					candi.setDNI(row.getCell(3).getStringCellValue());
					col = 4;
					candi.setApellido1(row.getCell(4).getStringCellValue());
					col = 5;
					candi.setApellido2(row.getCell(5).getStringCellValue());
					col = 6;
					candi.setNombre(row.getCell(6).getStringCellValue());
					col = 7;
					if (row.getCell(7) != null)
						row.getCell(7).setCellType(CellType.STRING);
					if (row.getCell(8) != null)
						row.getCell(8).setCellType(CellType.STRING);

					String numero1 = (row.getCell(7) == null) ? "" : row.getCell(7).getStringCellValue();
					col = 8;
					String numero2 = (row.getCell(8) == null) ? "" : row.getCell(8).getStringCellValue();

					candi.setTelefono(numero1 + "/" + numero2);
					col = 9;
					candi.setEmail((row.getCell(9) == null) ? "" : row.getCell(9).getStringCellValue());

					candi.setOcu(ocupacion.getIdOcupacion());

					candi.setCategoria(idCategoria);

					candi.setGc(gc);
					col = 13;
					candi.setEntidad((long) row.getCell(13).getNumericCellValue());

					candi.setEstado("PRE-CANDIDATO");

					candi.setFechaRegistro(LocalDate.now());

					candi.setIdPlan(idPlan);

					candi.setNacionalidad("");

				}

				candidatos.add(candi);

			}
			wb.close();

		} catch (MalformedURLException e) {
			throw new MyFileNotFoundException("No se encuentra el fichero excel");
		} catch (FileNotFoundException e) {
			throw new MyFileNotFoundException("No se encuentra el fichero excel");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcelParseErrorException(i, col, e.getMessage());
		}
		return candidatos;
	}

//	private List<CargaCandidatoDTO> excelToCargaCandidatoDTOList(Path fileStorageLocation) {
//		Workbook wb;
//		List<CargaCandidatoDTO> candidatos =new  ArrayList<CargaCandidatoDTO>();
//		try {
//			Resource resource = new UrlResource(fileStorageLocation.toUri());
//			
//			InputStream inp = new FileInputStream(resource.getFile());
//		    wb = WorkbookFactory.create(inp);
//		    Sheet sheet= wb.getSheetAt(0);
//		    
//		    int lastRow = sheet.getLastRowNum()+1;
//		    
//		    
//		    
//		    for (int i = 1; i < lastRow; i++) {
//				
//		    	 Row row = sheet.getRow(i);
//		    	 
//		    	 CargaCandidatoDTO candidato = new CargaCandidatoDTO();
//		    	 
//		    	 candidato.setOrdenSEPE(row.getCell(0).getStringCellValue());
//		    	 candidato.setFechaListadoSEPE(row.getCell(1).getDateCellValue());
//		    	 candidato.setSuplente(row.getCell(2).getStringCellValue());
//		    	 candidato.setDni(row.getCell(3).getStringCellValue());
//		    	 candidato.setNombre(row.getCell(4).getStringCellValue());
//		    	 candidato.setApellido1(row.getCell(5).getStringCellValue());
//		    	 candidato.setApellido2(row.getCell(6).getStringCellValue());
//		    	 candidato.setTelefono(row.getCell(7).getStringCellValue()+ "/" +row.getCell(8).getStringCellValue());
//		    	 candidato.setEmail(row.getCell(9).getStringCellValue());
//		    	 candidato.setIdOcupacion(row.getCell(10).getStringCellValue());
//		    	 candidato.setIdOrganismo(row.getCell(11).getStringCellValue());
//		    	 
//		    	 candidatos.add(candidato);
//		    
//		    	 
//		    	 
//			}
//		    wb.close();
//		  
//		   
//			
//		} catch (MalformedURLException e) {
//			throw new MyFileNotFoundException("No se encuentra el fichero excel");
//		}catch(FileNotFoundException e) {
//			throw new MyFileNotFoundException("No se encuentra el fichero excel");
//		}
//		catch(IOException e) {
//			
//		}
//		return candidatos;
//	}

}
