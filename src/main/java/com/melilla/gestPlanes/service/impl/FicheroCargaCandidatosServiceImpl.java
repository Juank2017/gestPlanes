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
import java.util.Optional;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.FicheroCargaCandidatosResponseDTO;
import com.melilla.gestPlanes.exceptions.exceptions.ExcelParseErrorException;
import com.melilla.gestPlanes.exceptions.exceptions.FicheroCandidatosUploadException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.OcupacionNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.ErroresCargaFicheroCandidatos;
import com.melilla.gestPlanes.model.FicheroCargaCandidatos;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.repository.ErroresCargaFicheroRepository;
import com.melilla.gestPlanes.repository.FicheroCargaCandidatosRepository;
import com.melilla.gestPlanes.repository.OcupacionRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.FicheroCargaCandidatosService;
import com.melilla.gestPlanes.service.PlanConfigService;
import com.melilla.gestPlanes.service.PlanService;
import com.melilla.gestPlanes.util.DNIValidator;

import lombok.extern.java.Log;

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

	@Autowired
	ErroresCargaFicheroRepository erroresRepository;

	@Override
	public FicheroCargaCandidatos subirFichero(MultipartFile fichero) {
		
		
		//Comprueba si el fichero es excel
		if (!fichero.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {

			throw new FicheroCandidatosUploadException();
		}

		FicheroCargaCandidatos ficheroCargaCandidatos = new FicheroCargaCandidatos();

		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir()).toAbsolutePath().normalize();

		try {
			
			//crea el directorio configurado en uploadTemplateDir
			Files.createDirectories(fileStorageLocation);

			// nombre del fichero
			String fileName = StringUtils.cleanPath(fichero.getOriginalFilename());
			
			
			if (fileName.contains("..")) {
				throw new FileStorageException(
						"El nombre de archivo tiene una secuencia de carácteres no válida " + fileName);
			}

			//Obtiene el path completo del fichero
			Path targetLocation = fileStorageLocation.resolve(fileName);

			//Hace la copia del fichero subido al directorio si ya existe lanza excepción
			Files.copy(fichero.getInputStream(), targetLocation);

			//Calcula la ruta para poderlo descargar
			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path(config.getUploadTemplateDir() + "/").path(fileName).toUriString();

			//guarda los datos del fichero en la base de datos.
			ficheroCargaCandidatos.setIdPlan(planService.getWorikingPlan());
			ficheroCargaCandidatos.setFileName(fileName);
			ficheroCargaCandidatos.setProcesado(false);
			ficheroCargaCandidatos.setURL(fileDownladUri);

			ficheroCargaCandidatos = ficheroCargaCandidatosRepository.save(ficheroCargaCandidatos);

		} catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo " + fichero.getResource().getFilename()+ " ya existe");
		} catch (Exception IOException) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		return ficheroCargaCandidatos;
	}

	@Override
	public void borrarFichero(long idFichero) {
		
		//busca los datos del fichero en la base de datos
		FicheroCargaCandidatos fichero = ficheroCargaCandidatosRepository.findById(idFichero)
				.orElseThrow(() -> new MyFileNotFoundException("Fichero no encontrado"));

		//Obtiene el nombre del directorio 
		PlanConfig config = planConfigService.obtenerConfig(planService.getWorikingPlan().getIdPlan());

		//Monta el path completo
		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "\\" + fichero.getFileName())
				.toAbsolutePath().normalize();
		try {

			//Borra el fichero físico y los datos del fichero en la base de datos.
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

		Path fileStorageLocation = Paths.get(config.getUploadTemplateDir() + "//" + fichero.getFileName())
				.toAbsolutePath().normalize();

		List<Ciudadano> creados = new ArrayList<Ciudadano>();

		List<CreateTrabajadorDTO> candidatosExtraidosDelExcel = new ArrayList<CreateTrabajadorDTO>();

		excelToCargaCandidatoDTOList(fileStorageLocation, candidatosExtraidosDelExcel, response.getCandidatosConError(),
				response.getErrores());

		Iterator<CreateTrabajadorDTO> it = candidatosExtraidosDelExcel.iterator();

		int contador = 0;
		while (it.hasNext()) {

			CreateTrabajadorDTO candidato = it.next();

			String dni = candidato.getDNI();

			try {
				if (DNIValidator.validate(dni)) {

					Ciudadano ciudadano = ciudadanoService.crearTrabajador(candidato);

					creados.add(ciudadano);

				} else {
					response.getErrores().add(contador + " DNI erróneo: " + candidato.getApellido1() + " "
							+ candidato.getApellido2() + "," + candidato.getNombre());
					response.getCandidatosConError().add(candidato);
				}

			} catch (OcupacionNotFoundException e) {

				response.getCandidatosConError().add(candidato);
				e.printStackTrace();

			} catch (Exception e) {

				response.getCandidatosConError().add(candidato);
				e.printStackTrace();

			}
			contador++;
		}
		fichero.setProcesado(true);

		if (response.getErrores().size() > 0) {
			fichero.setConError(true);

			List<String> errores = response.getErrores();

			errores.forEach((e) -> {
				erroresRepository.save(new ErroresCargaFicheroCandidatos(e, fichero));
			});
			ficheroCargaCandidatosRepository.save(fichero);
		}

		response.setCandidatos(ciudadanoService.listadoCiudadanosToListadoTrabajadoresDTO(creados));

		return response;
	}

	private void excelToCargaCandidatoDTOList(Path fileStorageLocation,
			List<CreateTrabajadorDTO> candidatosExtraidosDelExcel, List<CreateTrabajadorDTO> candidatosConError,
			List<String> errores) {
		Workbook wb;
		int i = 0;
		int j = 0;

		long idPlan = planService.getWorikingPlan().getIdPlan();

		try {

			Resource resource = new UrlResource(fileStorageLocation.toUri());

			InputStream inp = new FileInputStream(resource.getFile());

			wb = WorkbookFactory.create(inp);

			Sheet sheet = wb.getSheetAt(0);

			int lastRow = sheet.getLastRowNum();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			LocalDate fechaOrigen = LocalDate.of(1899, 12, 31);

			for (i = 1; i <= lastRow; i++) {

				Row row = sheet.getRow(i);
				
				if (row == null ) {
					errores.add("La fila es nula "+i);
					continue;
				}

				if (row.getCell(0) == null  || row.getCell(1) == null || row.getCell(2) == null || row.getCell(3) == null
						|| row.getCell(6) == null || row.getCell(5) == null || row.getCell(7) == null
						|| row.getCell(8) == null|| row.getCell(9) == null|| row.getCell(10) == null) {
					errores.add("Celda vacía en la línea: " + i);
				} else {
					CreateTrabajadorDTO candi = CreateTrabajadorDTO.builder().build();
					// Número orden SEPE
					if (row.getCell(0).getCellType().equals(CellType.NUMERIC)) {
						candi.setNumeroOrdenSepe((int) row.getCell(0).getNumericCellValue());
					} else {

						errores.add("El contenido de la celda en la línea:" + i
								+ " col: 0  no es un número. Tipo actual: " + row.getCell(0).getCellType());
					}
					// Fecha listado SEPE
					if (row.getCell(1).getCellType().equals(CellType.NUMERIC)) {
						candi.setFechaListadoSepe(
								fechaOrigen.plusDays((long) row.getCell(1).getNumericCellValue() - 1));
					} else {

						errores.add("El contenido de la celda en la línea:" + i + " col: 1 no es válido ");
					}
					// Suplente

					if (row.getCell(1).getCellType().equals(CellType.STRING)) {
						
						if (row.getCell(1).getStringCellValue().equals("NO")) {
							
							candi.setSuplente(false);
							candi.setEstado("PRE-CANDIDATO");
							
						}else{
							
							candi.setSuplente(true);
							candi.setEstado("NO-SELECCIONADO");
							
							
							
						}
						
						
						

					} else {

						errores.add("El contenido de la celda en la línea:" + i
								+ " col: 1 es un número, bebe ser SI o NO ");
					}
					
					// DNI
					if (row.getCell(2).getCellType().equals(CellType.STRING)) {
						String DNI = row.getCell(2).getStringCellValue();
						if (DNIValidator.validate(DNI)) {
							candi.setDNI(DNI);
						} else {
							errores.add("DNI erróneo:" + i + " col: 2 " + DNI);
						}
					} else {

						errores.add("El contenido de la celda en la línea:" + i
								+ " col: 2 es un número, ¿Falta la letra? ");
					}
					// APELLIDO1
					if (row.getCell(3).getCellType().equals(CellType.STRING)) {

						candi.setApellido1(row.getCell(3).getStringCellValue());

					} else {

						errores.add("El contenido de la celda en la línea:" + i + " col: 3 no es un texto. ");
					}
					// APELLIDO2
					if (row.getCell(4).getCellType().equals(CellType.STRING)) {

						if (row.getCell(4) != null) {
							candi.setApellido2(row.getCell(4).getStringCellValue());
						}

					} else {

						errores.add("El contenido de la celda en la línea:" + i + " col: 4 no es un texto. ");
					}
					// NOMBRE
					if (row.getCell(5).getCellType().equals(CellType.STRING)) {

						candi.setNombre(row.getCell(5).getStringCellValue());

					} else {

						errores.add("El contenido de la celda en la línea:" + i + " col: 5 no es un texto. ");
					}
					// Fecha listado SEPE
					if (row.getCell(6).getCellType().equals(CellType.NUMERIC)) {
						candi.setFechaListadoSepe(
								fechaOrigen.plusDays((long) row.getCell(6).getNumericCellValue() - 1));
					} else {

						errores.add("El contenido de la celda en la línea:" + i + " col: 6 no es válido ");
					}
			
					if (row.getCell(9) != null) {
						Long idOcupacion = (long) row.getCell(9).getNumericCellValue();

						Optional<Ocupacion> ocupacion = ocupacionRepository.findById(idOcupacion);

						if (ocupacion.isPresent()) {
							long idCategoria = ocupacion.get().getCategoria().getIdCategoria();

							long gc = (long) ocupacion.get().getCategoria().getGrupo();
							candi.setOcu(ocupacion.get().getIdOcupacion());

							candi.setCategoria(idCategoria);

							candi.setGc(gc);
						} else {
							errores.add("No se ha encontrado la ocupación de la línea: " + i);
						}

					}
					
					// Telefono
					if (row.getCell(12) != null)
						row.getCell(12).setCellType(CellType.STRING);
					if (row.getCell(13) != null)
						row.getCell(13).setCellType(CellType.STRING);

					String numero1 = (row.getCell(12) == null) ? "" : row.getCell(12).getStringCellValue();

					String numero2 = (row.getCell(13) == null) ? "" : row.getCell(13).getStringCellValue();

					candi.setTelefono(numero1 + "/" + numero2);
					
					if (row.getCell(14) != null)
						row.getCell(14).setCellType(CellType.STRING);

					candi.setEmail((row.getCell(14) == null) ? "" : row.getCell(14).getStringCellValue());

				
					if(row.getCell(10) != null && !row.getCell(10).getStringCellValue().isEmpty()) {
						candi.setEntidad((long) row.getCell(13).getNumericCellValue());
					}
					

					candi.setEstado("PRE-CANDIDATO");

					candi.setFechaRegistro(LocalDate.now());

					candi.setIdPlan(idPlan);

					candi.setNacionalidad("");

					candidatosExtraidosDelExcel.add(candi);
				}

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
			throw new ExcelParseErrorException(i, 0, e.getMessage());
		}

	}

}
