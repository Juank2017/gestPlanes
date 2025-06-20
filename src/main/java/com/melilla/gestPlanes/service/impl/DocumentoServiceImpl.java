package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.DTO.DocumentoAZip;
import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.DTO.GeneraContratoDTO;
import com.melilla.gestPlanes.DTO.GeneraContratoResponseDTO;
import com.melilla.gestPlanes.DTO.GeneraPresentacionDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PlanConfigErrorException;
import com.melilla.gestPlanes.exceptions.exceptions.PresentacionNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Presentacion;
import com.melilla.gestPlanes.model.TipoDocumento;
import com.melilla.gestPlanes.model.TipoDocumentoPlan;
import com.melilla.gestPlanes.model.config.PlanConfig;
import com.melilla.gestPlanes.model.config.PlantillaContratoConfig;
import com.melilla.gestPlanes.repository.CiudadanoRepository;
import com.melilla.gestPlanes.repository.DocumentoPlanRepository;
import com.melilla.gestPlanes.repository.DocumentoPlanSpecificationBuilder;
import com.melilla.gestPlanes.repository.DocumentoRepository;
import com.melilla.gestPlanes.repository.DocumentoSpecificationBuilder;
import com.melilla.gestPlanes.repository.PresentacionRepository;
import com.melilla.gestPlanes.repository.TipoDocumentoPlanRepository;
import com.melilla.gestPlanes.repository.TipoDocumentoRepository;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.DocumentoService;
import com.melilla.gestPlanes.service.PlanService;
import com.melilla.gestPlanes.service.PlantillaContratoConfigService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class DocumentoServiceImpl implements DocumentoService {

	@Autowired
	private CiudadanoService ciudadanoService;

	@Autowired
	private CiudadanoRepository ciudadanoRepository;

	@Autowired
	private DocumentoRepository documentoRepository;

	@Autowired
	private DocumentoPlanRepository documentoPlanRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private TipoDocumentoPlanRepository tipoDocumentoPlanRepository;

	@Autowired
	private PresentacionRepository presentacionRepository;

	@Autowired
	private PlanConfigServiceImpl planConfigService;

	@Autowired
	private PlantillaContratoConfigService plantillaContratoConfigService;

	@Autowired
	PlanService planService;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Value("${file.trashcan-dir}")
	private String trashcanDir;

	@Value("${file.contrato}")
	private String plantillaContrato;

	@Value("${file.presentacion}")
	private String plantillaPresentacion;

	@Autowired
	ResourceLoader resourceLoader;

	@Autowired
	PlanService planservice;

	@Override
	public Documento guardarDocumento(Long idCiudadano, MultipartFile file, String tipo) {

		String ocupacion;
		String nombreCarpeta;
		String estado;
		String apellido = "_";
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		// Obtiene el ciudadano
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		estado = ciudadano.getEstado().replace("/", "_") + "\\";

		switch (estado) {
		case "FINALIZADO_A\\":
			log.warning("case: " + estado);
			estado = "CONTRATADO_A\\";
			break;
		case "DESPEDIDO_A\\":

			estado = "CONTRATADO_A\\";
			break;

		case "RENUNCIA\\":

			estado = "CONTRATADO_A\\";
			break;

		}
		log.warning(estado);
		if (ciudadano.getContrato() != null) {
			// ocupacion del ciudadano
			Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
			ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
			// obtiene el apellido y sustituye los espacios por _
			apellido = ciudadano.getApellido1().replace(" ", "_");
			// forma el nombre de la capeta con apellidos_nombre
			nombreCarpeta = estado + ocupacion + apellido + "_" + ciudadano.getApellido2().replace(" ", "_") + "_"
					+ ciudadano.getNombre().replace(" ", "_");
		} else {

			nombreCarpeta = estado + apellido + "_" + ciudadano.getApellido2().replace(" ", "_") + "_"
					+ ciudadano.getNombre().replace(" ", "_");
		}

		// obtiene el path absoluto debe ser S:\PLANES DE
		// EMPLEO\ocupacion\apellidos_nombre
		Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta).toAbsolutePath().normalize();
		log.info(fileStorageLocation.toString());
		// Intenta crear el directorio si no existe.
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		// nombre del fichero
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		fileName = tipo + "_" + fileName;
		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException(
						"El nombre de archivo tiene una secuencia de carácteres no válida " + fileName);
			}
			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation);

			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
					.path(fileName).toUriString();

			Documento documento = new Documento();

			documento.setCiudadano(ciudadano);
			documento.setNombre(fileName);
			documento.setRuta(fileDownladUri);
			documento.setIdPlan(planService.getPlanActivo());
			documento.setTipo(tipo);

			return documento;
		} catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo " + fileName + " ya existe");
		} catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}

	}

	@Override
	public DocumentoPlan guardarDocumentoPlan(Long idPlan, MultipartFile file, String tipo) {

		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		String nombreCarpeta = "PLAN";

		// obtiene el path absoluto debe ser S:\PLANES DE
		// EMPLEO\ocupacion\apellidos_nombre
		nombreCarpeta = nombreCarpeta + "\\" + tipo;
		Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta).toAbsolutePath().normalize();
		log.info(fileStorageLocation.toString());
		// Intenta crear el directorio si no existe.
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

			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumentoPlan/")
					.path(fileName).toUriString();

			DocumentoPlan documento = new DocumentoPlan();

			documento.setNombre(fileName);
			documento.setRuta(fileDownladUri);
			documento.setIdPlan(planService.getPlanActivo());
			documento.setTipo(tipo);

			return documentoPlanRepository.save(documento);
		} catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo " + fileName + " ya existe");
		} catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}

	}

	@Override
	public Resource loadDocumentAsResource(Long idCiudadano, String filename, Long idDocumento) {
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		String estado = null;
		String apellido = "_";
		Documento doc = documentoRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		// obtiene el apellido y sustituye los espacios por _
		apellido = (ciudadano.getApellido1() != null) ? ciudadano.getApellido1().replace(" ", "_") : "null";
		estado = ciudadano.getEstado().replace("/", "_") + "\\";
		Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
		String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_").replace("/", "_") + "\\";
		String nombreCarpeta = estado + ocupacion + ciudadano.getApellido1().replace(" ", "_") + "_"
				+ ciudadano.getApellido2().replace(" ", "_") + "_" + ciudadano.getNombre().replace(" ", "_") + "\\";
		try {
			Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta + filename).toAbsolutePath()
					.normalize();
			log.info(fileStorageLocation.toString());
			log.info(fileStorageLocation.toUri().toString());
			Resource resource = new UrlResource(fileStorageLocation.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + filename);
		}
	}

	@Override
	public Resource loadDocumentPlanAsResource(String filename, Long idDocumento) {
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		DocumentoPlan doc = documentoPlanRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));

		String nombreCarpeta = "PLAN\\" + doc.getTipo() + "\\";
		try {
			Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta + filename).toAbsolutePath()
					.normalize();
			log.info(fileStorageLocation.toString());
			log.info(fileStorageLocation.toUri().toString());
			Resource resource = new UrlResource(fileStorageLocation.toUri());

			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + filename);
		}
	}

	@Override
	public void eliminarDocumento(Long idDocumento) {
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		String estado = null;
		String apellido = "_";
		Documento doc = documentoRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		String filename = doc.getNombre();
		Ciudadano ciudadano = doc.getCiudadano();
		// obtiene el apellido y sustituye los espacios por _
		apellido = ciudadano.getApellido1().replace(" ", "_");
		estado = ciudadano.getEstado().replace("/", "_") + "\\";
		Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
		String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_").replace("/", "_") + "\\";
		String nombreCarpeta = estado + ocupacion + ciudadano.getApellido1() + "_"
				+ ciudadano.getApellido2().replace(" ", "_") + "_" + ciudadano.getNombre().replace(" ", "_") + "\\";
		try {
			Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta + filename).toAbsolutePath()
					.normalize();
			log.info(fileStorageLocation.toString());
			log.info(fileStorageLocation.toUri().toString());
			// Resource resource = new UrlResource(fileStorageLocation.toUri());
			Resource resource = loadDocumentAsResource(ciudadano.getIdCiudadano(), filename, idDocumento);

			if (resource.exists()) {
				File fichero = resource.getFile();

				// log.info(config.getTrashcanDir() + Instant.now().toEpochMilli()+"_" +
				// fichero.getName());
				Path fileTrashcanLocartion = Paths
						.get(config.getTrashcanDir() + Instant.now().toEpochMilli() + "_" + fichero.getName())
						.toAbsolutePath().normalize();
				log.info("trash");
				log.info(fileTrashcanLocartion.toString());
				Files.move(Paths.get(fichero.getAbsolutePath()), fileTrashcanLocartion,
						StandardCopyOption.REPLACE_EXISTING);
				documentoRepository.deleteById(idDocumento);
			} else {

				throw new MyFileNotFoundException("File not found " + filename);
			}
		} catch (NoSuchFileException ex) {
			ex.printStackTrace();
			throw new MyFileNotFoundException("File not found " + filename);
		}

		catch (MalformedURLException ex) {
			ex.printStackTrace();
			throw new MyFileNotFoundException("File not found " + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ex) {
			throw new FileStorageException(ex.getMessage());
		}

	}

	@Override
	public void eliminarDocumentoPlan(Long idDocumento) {
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		String nombreCarpeta = "PLAN";
		DocumentoPlan doc = documentoPlanRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		String filename = doc.getNombre();

		nombreCarpeta = nombreCarpeta + "\\" + doc.getTipo() + "\\";
		try {
			Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta + filename).toAbsolutePath()
					.normalize();
			log.info(fileStorageLocation.toString());
			log.info(fileStorageLocation.toUri().toString());
			Resource resource = new UrlResource(fileStorageLocation.toUri());

			if (resource.exists()) {
				File fichero = resource.getFile();
				Path fileTrashcanLocartion = Paths
						.get(config.getTrashcanDir() + Instant.now().toEpochMilli() + "_" + fichero.getName())
						.toAbsolutePath().normalize();
				Files.move(fileStorageLocation, fileTrashcanLocartion, StandardCopyOption.REPLACE_EXISTING);
				documentoPlanRepository.deleteById(idDocumento);
			} else {
				throw new MyFileNotFoundException("File not found " + filename);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public Documento guardarBBDD(Documento documento) {

		return documentoRepository.save(documento);
	}

	
	@Override
	public List<GeneraContratoResponseDTO> generarContrato(List<GeneraContratoDTO> trabajadores) {

		String directorioSubida = null;

		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());

		if (config.getUploadDir() == null || config.getUploadDir().equals("")) {
			throw new PlanConfigErrorException();
		} else {
			directorioSubida = config.getUploadDir();
		}

		List<GeneraContratoResponseDTO> listaContratosGenerados = new ArrayList<>();

		try {
			log.warning("Inicia genera contrato");
			// carga el fichero de la plantilla de resources

			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaContrato);
			File plantilla = classPahtResource.getFile();
			for (GeneraContratoDTO generaContratoDTO : trabajadores) {
				log.warning("Inicio Genera contrato: " + generaContratoDTO.getId());
				// Carga el trabajador
				Ciudadano trabajador = ciudadanoRepository.findById(generaContratoDTO.getId())
						.orElseThrow(() -> new CiudadanoNotFoundException(generaContratoDTO.getId()));

				if (trabajador.getContrato() == null)
					continue;
				// extrae el contrato del trabajador
				Contrato contrato = trabajador.getContrato();

				// carga la plantilla como pdf
				PDDocument nuevoContrato = PDDocument.load(plantilla);
				nuevoContrato.setAllSecurityToBeRemoved(true);
				// obtiene el formulario del documento
				PDAcroForm formulario = nuevoContrato.getDocumentCatalog().getAcroForm();

				// Rellena la cabecera del contrato

				// formulario.getField("AA0101-DNI").setValue("S2916002E");

				formulario = rellenaFormulario(trabajador, contrato, nuevoContrato, formulario);

				// nombre del fichero
				String nombreFichero = trabajador.getApellido1().replace(" ", "_") + "_"
						+ trabajador.getApellido2().replace(" ", "_") + "_" + trabajador.getNombre().replace(" ", "_")
						+ "_" + trabajador.getDNI() + "_CONTRATO.pdf";

				// carpeta
				// ocupacion del ciudadano
				Ocupacion ocupacionCiudadano = trabajador.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_").replace("/", "_") + "\\";
				// estado
				String estado = trabajador.getEstado().replace("/", "_") + "\\";
				// forma el nombre de la capeta con apellidos_nombre
				String nombreCarpeta = estado + ocupacion + trabajador.getApellido1().replace(" ", "_") + "_"
						+ trabajador.getApellido2().replace(" ", "_") + "_" + trabajador.getNombre().replace(" ", "_");
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(directorioSubida + nombreCarpeta).normalize();
				log.info(fileStorageLocation.toString());
				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}

				Path fichero = Paths.get(directorioSubida + nombreCarpeta + "\\" + nombreFichero).normalize();
				log.info(fichero.toString());
				String contratoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					log.info("Existe el fichero");
					nombreFichero = nombreFichero.replace("_CONTRATO", "_" + Instant.now().toEpochMilli());
					log.info(nombreFichero);
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				} else {
					log.info("no existe");
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				}
				;
				log.warning("Genera contrato guardando el pdf a disco: " + generaContratoDTO.getId());
				nuevoContrato.save(contratoParaGuardar);
				nuevoContrato.close();
				log.warning("Genera contrato guardado el pdf a disco: " + generaContratoDTO.getId());
				String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
						.path(nombreFichero).toUriString();

				Documento documento = new Documento();
				documento.setIdPlan(planService.getPlanActivo());
				documento.setCiudadano(trabajador);
				documento.setNombre(nombreFichero);
				documento.setRuta(fileDownladUri);
				documento.setTipo("CONTRATO");

				GeneraContratoResponseDTO response = new GeneraContratoResponseDTO();
				response.setIdCiudadano(trabajador.getIdCiudadano());
				response.setNombre(trabajador.getNombre());
				response.setApellido1(trabajador.getApellido1());
				response.setApellido2(trabajador.getApellido2());
				response.setDNI(trabajador.getDNI());
				response.setDocumento(guardarBBDD(documento));

				listaContratosGenerados.add(response);

				trabajador.getDocumentos().add(documento);
				ciudadanoService.crearCiudadano(trabajador);
				log.warning("fin Genera contrato: " + generaContratoDTO.getId());
			}

		} catch (Exception e) {
			log.warning(e.getMessage());
			e.printStackTrace();
			throw new DocumentCreationException(e.getMessage());
		}
		return listaContratosGenerados;

	}

	@Override
	public List<GeneraContratoResponseDTO> generarContratoConPlantilla(List<GeneraContratoDTO> trabajadores) {
		String directorioSubida = null;

		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());

		if (config.getUploadDir() == null || config.getUploadDir().equals("")) {
			throw new PlanConfigErrorException();
		} else {
			directorioSubida = config.getUploadDir();
		}

		List<GeneraContratoResponseDTO> listaContratosGenerados = new ArrayList<>();

		try {
			log.warning("Inicia genera contrato");
			// carga el fichero de la plantilla de resources
			String directorioPlantillas = config.getTemplateDir();
			PlantillaContratoConfig plantillaContrato = plantillaContratoConfigService.obtenerPlantillaActiva(planservice.getPlanActivo());
			Resource classPahtResource = resourceLoader.getResource("file:" +directorioPlantillas+"\\"+ plantillaContrato.getNombreFicheroPlantilla());
			File plantilla = classPahtResource.getFile();
			for (GeneraContratoDTO generaContratoDTO : trabajadores) {
				log.warning("Inicio Genera contrato: " + generaContratoDTO.getId());
				// Carga el trabajador
				Ciudadano trabajador = ciudadanoRepository.findById(generaContratoDTO.getId())
						.orElseThrow(() -> new CiudadanoNotFoundException(generaContratoDTO.getId()));

				if (trabajador.getContrato() == null)
					continue;
				// extrae el contrato del trabajador
				Contrato contrato = trabajador.getContrato();

				// carga la plantilla como pdf
				PDDocument nuevoContrato = PDDocument.load(plantilla);
				nuevoContrato.setAllSecurityToBeRemoved(true);
				// obtiene el formulario del documento
				PDAcroForm formulario = nuevoContrato.getDocumentCatalog().getAcroForm();

				HashMap<String, String> datosFormulario = generarInfoContrato(trabajador, contrato);

				formulario = rellenaFormularioConPlantilla(nuevoContrato, formulario, datosFormulario);

				// nombre del fichero
				String nombreFichero = trabajador.getApellido1().replace(" ", "_") + "_"
						+ trabajador.getApellido2().replace(" ", "_") + "_" + trabajador.getNombre().replace(" ", "_")
						+ "_" + trabajador.getDNI() + "_CONTRATO.pdf";

				// carpeta
				// ocupacion del ciudadano
				Ocupacion ocupacionCiudadano = trabajador.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_").replace("/", "_") + "\\";
				// estado
				String estado = trabajador.getEstado().replace("/", "_") + "\\";
				// forma el nombre de la capeta con apellidos_nombre
				String nombreCarpeta = estado + ocupacion + trabajador.getApellido1().replace(" ", "_") + "_"
						+ trabajador.getApellido2().replace(" ", "_") + "_" + trabajador.getNombre().replace(" ", "_");
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(directorioSubida + nombreCarpeta).normalize();
				log.info(fileStorageLocation.toString());
				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}

				Path fichero = Paths.get(directorioSubida + nombreCarpeta + "\\" + nombreFichero).normalize();
				log.info(fichero.toString());
				String contratoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					log.info("Existe el fichero");
					nombreFichero = nombreFichero.replace("_CONTRATO", "_" + Instant.now().toEpochMilli());
					log.info(nombreFichero);
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				} else {
					log.info("no existe");
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				}
				;
				log.warning("Genera contrato guardando el pdf a disco: " + generaContratoDTO.getId());
				nuevoContrato.save(contratoParaGuardar);
				nuevoContrato.close();
				log.warning("Genera contrato guardado el pdf a disco: " + generaContratoDTO.getId());
				String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
						.path(nombreFichero).toUriString();

				Documento documento = new Documento();
				documento.setIdPlan(planService.getPlanActivo());
				documento.setCiudadano(trabajador);
				documento.setNombre(nombreFichero);
				documento.setRuta(fileDownladUri);
				documento.setTipo("CONTRATO");

				GeneraContratoResponseDTO response = new GeneraContratoResponseDTO();
				response.setIdCiudadano(trabajador.getIdCiudadano());
				response.setNombre(trabajador.getNombre());
				response.setApellido1(trabajador.getApellido1());
				response.setApellido2(trabajador.getApellido2());
				response.setDNI(trabajador.getDNI());
				response.setDocumento(guardarBBDD(documento));

				listaContratosGenerados.add(response);

				trabajador.getDocumentos().add(documento);
				ciudadanoService.crearCiudadano(trabajador);
				log.warning("fin Genera contrato: " + generaContratoDTO.getId());
			}

		} catch (Exception e) {
			log.warning(e.getMessage());
			e.printStackTrace();
			throw new DocumentCreationException(e.getMessage());
		}
		return listaContratosGenerados;
	}

	private HashMap<String, String> generarInfoContrato(Ciudadano trabajador, Contrato contrato) {

		PlantillaContratoConfig plantilla = plantillaContratoConfigService
				.obtenerPlantillaActiva(planservice.getPlanActivo());

		HashMap<String, String> info = new HashMap<String, String>();

//		info.put("cif", plantilla.getCif());
//		info.put("nombreRepresentante", plantilla.getNombreRepresentante());
//		info.put("dniRepresentante", plantilla.getDniRepresentante());
//		info.put("cargoRepresentante", plantilla.getCargoRepresentante());
//		info.put("", plantillaContrato)

		if (trabajador.getFechaNacimiento() != null) {
			info.put("fechaNacimiento", trabajador.getFechaNacimiento()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES"))));

		} else {
			throw new DocumentCreationException("La fecha de nacimiento no puede ser nula");
		}

		String diaFirma = "00/00/0000";
		String anoFirma = "00/00/0000";
		String mesFechaFirma = "00/00/0000";
		String fechaInicio = "00/00/0000";
		String fechaFinal = "00/00/0000";
		if (contrato.getFechaInicio() != null && contrato.getFechaFinal() != null) {
			fechaInicio = contrato.getFechaInicio()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
			fechaFinal = contrato.getFechaFinal()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
			mesFechaFirma = contrato.getFechaInicio()
					.format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

			diaFirma = contrato.getFechaInicio().format(DateTimeFormatter.ofPattern("dd", new Locale("es", "ES")));
			anoFirma = contrato.getFechaInicio().format(DateTimeFormatter.ofPattern("uuuu", new Locale("es", "ES")));
		} else {
			throw new DocumentCreationException("Fecha alta o fecha baja incorrectas");
		}
		info.put("diaFirma", diaFirma);
		info.put("anoFirma", anoFirma);
		info.put("mesFechaFirma", mesFechaFirma);
		info.put("fechaInicio", fechaInicio);
		info.put("fechaFinal", fechaFinal);

		info.put("nombre", trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
		info.put("dni", trabajador.getDNI());

		info.put("segSocial", trabajador.getSeguridadSocial().replaceAll("/", ""));

		info.put("categoria", contrato.getCategoria().getCategoria());
		info.put("nacionalidad", trabajador.getNacionalidad());

		info.put("ocupacion", contrato.getOcupacion().getOcupacion());

		info.put("grupo_profesional", contrato.getCategoria().getGrupoProfesionalPersonalLaboral());

		info.put("total", contrato.getTotal());
		info.put("base", contrato.getBase());
		info.put("prorrata", contrato.getProrratas());
		info.put("residencia", contrato.getResidencia());

		info.put("sinClausula", trabajador.isSinClausula() + "");

		return info;
	}

	private PDAcroForm rellenaFormularioConPlantilla(PDDocument nuevoContrato, PDAcroForm formulario,
			HashMap<String, String> datosFormulario) throws IOException {

		// PDTextField field = (PDTextField) formulario.getField("AA0101-DNI");

		PlantillaContratoConfig plantilla = plantillaContratoConfigService
				.obtenerPlantillaActiva(planservice.getPlanActivo());

		Resource fuente = resourceLoader.getResource("classpath:Arial-BoldMT.ttf");
		PDFont font = PDType0Font.load(nuevoContrato, fuente.getInputStream(), false);
		log.warning(font.getName());
		PDResources resources = new PDResources();
		resources.add(font);

		formulario.setDefaultResources(resources);
		resources.getFontNames().forEach((f) -> log.warning(f.toString()));
		String defaultAppearanceString = "/F1 0 Tf 0 g";
		// field.setDefaultAppearance(defaultAppearanceString);

		formulario.getFields().forEach((f) -> {
			if (f instanceof PDTextField) {
				((PDTextField) f).setDefaultAppearance(defaultAppearanceString);
			}
		});

		formulario.getField("AA0101-DNI").setValue(plantilla.getCif());

		// field.setValue("44");
		formulario.getField("AA0102").setValue(plantilla.getNombreRepresentante());
		formulario.getField("AA0103-DNI").setValue(plantilla.getDniRepresentante());
		formulario.getField("AA0104").setValue(plantilla.getCargoRepresentante());
		formulario.getField("AA01005").setValue(plantilla.getRazonSocial());
		formulario.getField("AA01006").setValue(plantilla.getDomicilioSocial());
		formulario.getField("AA01007").setValue(plantilla.getPaisEmpresa());
		formulario.getField("AA0108-E3").setValue(plantilla.getCodigoPaisEmpresa());// CODIGO PAIS
		formulario.getField("AA01009").setValue(plantilla.getMunicipioEmpresa());
		formulario.getField("AA0110-E5").setValue(plantilla.getCodigoMunicipioEmpresa());
		formulario.getField("AA0111-E5").setValue(plantilla.getCodigoPostalEmpresa());

		// DATOS CUENTA COTIZACIÓN
		formulario.getField("AA0201-E4").setValue(plantilla.getRegimen());
		formulario.getField("AA0202-E11").setValue(plantilla.getCodigoCuentaCotizacion());
		formulario.getField("AA0203").setValue(plantilla.getActividadEconomica());

		// DATOS DEL CENTRO DE TRABAJO
		formulario.getField("AA0301").setValue(plantilla.getPaisEmpresa());
		formulario.getField("AA0302-E3").setValue(plantilla.getCodigoPaisEmpresa());
		formulario.getField("AA0303").setValue(plantilla.getMunicipioEmpresa());
		formulario.getField("AA0304-E5").setValue(plantilla.getCodigoMunicipioEmpresa());

		// DATOS TRABAJADOR

		formulario.getField("AA0401").setValue(datosFormulario.get("nombre"));
		formulario.getField("AA0402-DNI").setValue(datosFormulario.get("dni"));
		formulario.getField("AA0403-FE").setValue(datosFormulario.get("fechaNacimiento"));
		formulario.getField("AA0404-E12").setValue(datosFormulario.get("segSocial"));
		formulario.getField("AA0405").setValue(datosFormulario.get("categoria"));
		formulario.getField("AA0407").setValue(datosFormulario.get("nacionalidad"));
		formulario.getField("AA0409").setValue(plantilla.getMunicipioEmpresa());
		formulario.getField("AA0410-E5").setValue(plantilla.getCodigoPostalEmpresa());
		formulario.getField("AA0411").setValue(plantilla.getPaisEmpresa());
		formulario.getField("AA0412-E3").setValue(plantilla.getCodigoPaisEmpresa());

		// CLAUSULAS

		formulario.getField("C101").setValue(datosFormulario.get("ocupacion"));
		formulario.getField("C102").setValue(datosFormulario.get("categoria"));
		formulario.getField("C103").setValue(datosFormulario.get("ocupacion"));

		formulario.getField("C1004").setValue(plantilla.getMunicipioEmpresa());

		formulario.getField("C2_BO1").setValue("Elección2");// A tiempo parcial
		formulario.getField("C204").setValue(plantilla.getHoras());

		formulario.getField("C2_BO2").setValue("Elección2");// a la semana

		formulario.getField("C3_BO3").setValue("Elección2");

		formulario.getField("C301_FE").setValue(datosFormulario.get("fechaInicio"));
		formulario.getField("C302_FE").setValue(datosFormulario.get("fechaFinal"));

		if (datosFormulario.get("sinClausula").equals("false")) {
			formulario.getField("C303").setValue("UN MES");
		} else {
			formulario.getField("C303").setValue("SIN PERIODO DE PRUEBA");
		}

		formulario.getField("C401").setValue(datosFormulario.get("total"));
		formulario.getField("C402").setValue("MENSUALES");

		String conceptosSalariales = procesaTagsCadena(plantilla.getTextoConceptosSalariales(), datosFormulario);

		formulario.getField("C403").setValue(conceptosSalariales);

		formulario.getField("C501").setValue("30 DÍAS NATURALES");

		formulario.getField("C801").setValue(plantilla.getSepe());

		formulario.getField("P11CV1").setValue("Sí");
		formulario.getField("P11BO1").setValue("Elección2");
		formulario.getField("P11BO2").setValue("Elección2");

		String especifica = procesaTagsCadena(plantilla.getClausulaEspecifica(), datosFormulario);

		formulario.getField("P1108").setValue(especifica);

		String adicional = procesaTagsCadena(plantilla.getClausulaAdicional(), datosFormulario);

		// Literal contrato
		formulario.getField("P2301").setValue(adicional);

		formulario.getField("P2302").setValue("MELILLA");

		formulario.getField("P2303").setValue("" + datosFormulario.get("diaFirma"));
		formulario.getField("P2304").setValue(datosFormulario.get("mesFirma"));
		formulario.getField("P2305").setValue("" + datosFormulario.get("anoFirma"));

		formulario.flatten();

		return formulario;
	}

	/**
	 * Recibe una cadena y sustituye las subcadenas que empiezan y terminan por $
	 * por la correspondiente clave del hashmap.
	 * 
	 * @param cadena
	 * @param datosFormulario
	 * @return cadena
	 */
	private String procesaTagsCadena(String cadena, HashMap<String, String> datosFormulario) {
		if (!cadena.contains("$"))
			return cadena;
		String[] tags = org.apache.commons.lang3.StringUtils.substringsBetween(cadena, "$", "$");

		for (String string : tags) {

			if (datosFormulario.get(string) != null) {
				cadena = cadena.replace("$" + string + "$", datosFormulario.get(string));
			}

		}

		return cadena;

	}

	private PDAcroForm rellenaFormulario(Ciudadano trabajador, Contrato contrato, PDDocument nuevoContrato,
			PDAcroForm formulario) throws IOException {
		PDTextField field = (PDTextField) formulario.getField("AA0101-DNI");

		Resource fuente = resourceLoader.getResource("classpath:Arial-BoldMT.ttf");
		PDFont font = PDType0Font.load(nuevoContrato, fuente.getInputStream(), false);
		log.warning(font.getName());
		PDResources resources = new PDResources();
		resources.add(font);

		formulario.setDefaultResources(resources);
		resources.getFontNames().forEach((f) -> log.warning(f.toString()));
		String defaultAppearanceString = "/F1 0 Tf 0 g";
		field.setDefaultAppearance(defaultAppearanceString);

		formulario.getFields().forEach((f) -> {
			if (f instanceof PDTextField) {
				((PDTextField) f).setDefaultAppearance(defaultAppearanceString);
			}
		});

		// FORMATEO DE FECHAS

//				@SuppressWarnings("deprecation")
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", new Locale("es", "ES"));

		String fechaNacimiento = null;
		if (trabajador.getFechaNacimiento() != null) {
			fechaNacimiento = trabajador.getFechaNacimiento()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
		} else {
			throw new DocumentCreationException("La fecha de nacimiento no puede ser nula");
		}

		String mesFechaFirma = "00/00/0000";
		String fechaInicio = "00/00/0000";
		String fechaFinal = "00/00/0000";
		if (contrato.getFechaInicio() != null && contrato.getFechaFinal() != null) {
			fechaInicio = contrato.getFechaInicio()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
			fechaFinal = contrato.getFechaFinal()
					.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
			mesFechaFirma = contrato.getFechaInicio()
					.format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

		} else {
			throw new DocumentCreationException("Fecha alta o fecha baja incorrectas");
		}

		formulario.getField("AA0101-DNI").setValue("S2916002E");

		// field.setValue("44");
		formulario.getField("AA0102").setValue("SABRINA MOH ABDELKADER");
		formulario.getField("AA0103-DNI").setValue("45.281.593-K");
		formulario.getField("AA0104").setValue("DELEGADA DEL GOBIERNO");
		formulario.getField("AA01005").setValue("DELEGACIÓN DEL GOBIERNO EN MELILLA");
		formulario.getField("AA01006").setValue("AVDA.MARINA ESPAÑOLA 3");
		formulario.getField("AA01007").setValue("ESPAÑA");
		formulario.getField("AA0108-E3").setValue("724");// CODIGO PAIS
		formulario.getField("AA01009").setValue("MELILLA");
		formulario.getField("AA0110-E5").setValue("52001");
		formulario.getField("AA0111-E5").setValue("52001");

		// DATOS CUENTA COTIZACIÓN
		formulario.getField("AA0201-E4").setValue("0111");
		formulario.getField("AA0202-E11").setValue("52100759127");
		formulario.getField("AA0203").setValue("ADMINISTRACIÓN");

		// DATOS DEL CENTRO DE TRABAJO
		formulario.getField("AA0301").setValue("ESPAÑA");
		formulario.getField("AA0302-E3").setValue("724");
		formulario.getField("AA0303").setValue("MELILLA");
		formulario.getField("AA0304-E5").setValue("52001");

		// DATOS TRABAJADOR

		formulario.getField("AA0401")
				.setValue(trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
		formulario.getField("AA0402-DNI").setValue(trabajador.getDNI());
		formulario.getField("AA0403-FE").setValue(fechaNacimiento);
		formulario.getField("AA0404-E12").setValue(trabajador.getSeguridadSocial().replaceAll("/", ""));
		formulario.getField("AA0405").setValue(contrato.getCategoria().getCategoria());
		formulario.getField("AA0407").setValue(trabajador.getNacionalidad());
		formulario.getField("AA0409").setValue("MELILLA");
		formulario.getField("AA0410-E5").setValue("52001");
		formulario.getField("AA0411").setValue("ESPAÑA");
		formulario.getField("AA0412-E3").setValue("724");

		// CLAUSULAS

		formulario.getField("C101").setValue(contrato.getOcupacion().getOcupacion());
		formulario.getField("C102").setValue(contrato.getCategoria().getCategoria());
		formulario.getField("C103").setValue(contrato.getOcupacion().getOcupacion());

		formulario.getField("C1004").setValue("MELILLA");

		formulario.getField("C2_BO1").setValue("Elección2");// A tiempo parcial
		formulario.getField("C204").setValue("25,20");

		formulario.getField("C2_BO2").setValue("Elección2");// a la semana

		formulario.getField("C3_BO3").setValue("Elección2");

		formulario.getField("C301_FE").setValue(fechaInicio);
		formulario.getField("C302_FE").setValue(fechaFinal);

		if (!trabajador.isSinClausula()) {
			formulario.getField("C303").setValue("UN MES");
		} else {
			formulario.getField("C303").setValue("SIN PERIODO DE PRUEBA");
		}

		formulario.getField("C401").setValue(contrato.getTotal());
		formulario.getField("C402").setValue("MENSUALES");
		formulario.getField("C403").setValue("S.B.: " + contrato.getBase() + "€ + P.P.P.E.: " + contrato.getProrratas()
				+ "€ + Residencia: " + contrato.getResidencia() + "€. (Ver claúsula adicional 2ª) ");

		formulario.getField("C501").setValue("30 DÍAS NATURALES");

		formulario.getField("C801").setValue("MELILLA");

		formulario.getField("P11CV1").setValue("Sí");
		formulario.getField("P11BO1").setValue("Elección2");
		formulario.getField("P11BO2").setValue("Elección2");
		formulario.getField("P1108").setValue(
				"Programa común de inserción laboral a través de obras y servicios de interés general y social, recogido en la subsección 1ª de la sección 3ª del Capitulo V del Real Decreto 818/2021 de 28 de septiembre y la orden TES/1077/2023  de 28 de septiembre y la convocatoria para la concesión de subvenciones destinadas al anterior programa en colaboración con órganos de la AGE en el ámbito territorial de las ciudades de Ceuta y Melilla, aprobada por resolución de 13/11/2024 de la Dirección General del SEPE.");

		String grupo_profesional = (contrato.getGc().equals("5") ? "E2" : "E1");
		// Literal contrato
		formulario.getField("P2301").setValue(
				"1ª El presente contrato se formaliza para participar en los programas, o en su caso programa, contenidos en el documento de colaboración formalizado entre la Delegación del Gobierno y la entidad donde va a desarrollar la actividad laboral el trabajador contratado, para el desarrollo del Plan de Empleo 2024-2025.\n \n "
						+ "2ª Las retribuciones pactadas se corresponden a las retribuciones establecidas en el IV Convenio Único para el personal laboral de la Administracion General del Estado, calculadas para una jornada a tiempo parcial de 25,20 horas a la semana, para el grupo profesional "
						+ grupo_profesional + ".");

		formulario.getField("P2302").setValue("MELILLA");

		formulario.getField("P2303").setValue("" + contrato.getFechaInicio().getDayOfMonth());
		formulario.getField("P2304").setValue(mesFechaFirma.toUpperCase());
		formulario.getField("P2305").setValue("" + contrato.getFechaInicio().getYear());

		formulario.flatten();
		return formulario;
	}

	@Override
	public Documento obtenerDocumentoPorNombreIdCiudadano(String fileName, Long idCiduadano) {

		return null;
	}

	@Override
	public void downloadDocumentsAsZipFile(HttpServletResponse response, List<DocumentoAZip> docs) {

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=download.zip");
		try {

			ZipOutputStream zipOutput = new ZipOutputStream(response.getOutputStream());

			for (DocumentoAZip documentoAZip : docs) {

				Documento documento = documentoRepository.findById(documentoAZip.getIdDocumento())
						.orElseThrow(() -> new DocumentoNotFoundException(documentoAZip.getIdDocumento()));

				Resource ficheroAComprimir = loadDocumentAsResource(documentoAZip.getIdCiudadano(),
						documento.getNombre(), documento.getIdDocumento());

				ZipEntry zipEntry = new ZipEntry(ficheroAComprimir.getFilename());
				zipEntry.setSize(ficheroAComprimir.getFile().length());
				zipEntry.setTime(System.currentTimeMillis());
				zipOutput.putNextEntry(zipEntry);

				StreamUtils.copy(ficheroAComprimir.getInputStream(), zipOutput);
				zipOutput.closeEntry();

			}
			zipOutput.finish();

		} catch (Exception e) {
			log.info(e.getMessage());

		}

	}

	@Override
	public void downloadDocumentsPlanAsZipFile(HttpServletResponse response, List<DocumentoAZip> docs) {

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=download.zip");
		try {

			ZipOutputStream zipOutput = new ZipOutputStream(response.getOutputStream());

			for (DocumentoAZip documentoAZip : docs) {

				DocumentoPlan documento = documentoPlanRepository.findById(documentoAZip.getIdDocumento())
						.orElseThrow(() -> new DocumentoNotFoundException(documentoAZip.getIdDocumento()));

				Resource ficheroAComprimir = loadDocumentPlanAsResource(documento.getNombre(),
						documento.getIdDocumentoPlan());

				ZipEntry zipEntry = new ZipEntry(ficheroAComprimir.getFilename());
				zipEntry.setSize(ficheroAComprimir.getFile().length());
				zipEntry.setTime(System.currentTimeMillis());
				zipOutput.putNextEntry(zipEntry);

				StreamUtils.copy(ficheroAComprimir.getInputStream(), zipOutput);
				zipOutput.closeEntry();

			}
			zipOutput.finish();

		} catch (Exception e) {
			log.info(e.getMessage());

		}

	}

	@Override
	public List<GeneraContratoResponseDTO> buscarDocumentos(List<DocumentoCriterioBusqueda> criterios) {

		List<GeneraContratoResponseDTO> response = new ArrayList<GeneraContratoResponseDTO>();

		DocumentoSpecificationBuilder consulta = new DocumentoSpecificationBuilder(criterios, planService);

		List<Documento> documentos = documentoRepository.findAll(consulta.build());

		for (Documento documento : documentos) {

			GeneraContratoResponseDTO generaContratoResponseDTO = new GeneraContratoResponseDTO();
			if (documento.getCiudadano() != null) {
				generaContratoResponseDTO.setIdCiudadano(documento.getCiudadano().getIdCiudadano());

				generaContratoResponseDTO.setNombre(documento.getCiudadano().getNombre());
				generaContratoResponseDTO.setApellido1(documento.getCiudadano().getApellido1());
				generaContratoResponseDTO.setApellido2(documento.getCiudadano().getApellido2());
				generaContratoResponseDTO.setDNI(documento.getCiudadano().getDNI());
			}

			generaContratoResponseDTO.setDocumento(documento);

			response.add(generaContratoResponseDTO);

		}

		return response;
	}

	@Override
	public List<DocumentoPlan> buscarDocumentosPlan(List<DocumentoCriterioBusqueda> criterios) {

		// List<DocumentoPlan> response = new ArrayList<DocumentoPlan>();

		DocumentoPlanSpecificationBuilder consulta = new DocumentoPlanSpecificationBuilder(criterios, planService);

		List<DocumentoPlan> documentos = documentoPlanRepository.findAll(consulta.build());

//		for (Documento documento : documentos) {
//			DocumentoPlan documentoPlan = new DocumentoPlan();
//		//	GeneraContratoResponseDTO generaContratoResponseDTO = new GeneraContratoResponseDTO();
//			if (documento.getCiudadano() != null) {
//				generaContratoResponseDTO.setIdCiudadano(documento.getCiudadano().getIdCiudadano());
//
//				generaContratoResponseDTO.setNombre(documento.getCiudadano().getNombre());
//				generaContratoResponseDTO.setApellido1(documento.getCiudadano().getApellido1());
//				generaContratoResponseDTO.setApellido2(documento.getCiudadano().getApellido2());
//				generaContratoResponseDTO.setDNI(documento.getCiudadano().getDNI());
//			}
//
//			generaContratoResponseDTO.setDocumento(documento);
//
//			response.add(generaContratoResponseDTO);
//
//		}

		return documentos;
	}

	@Override
	public List<TipoDocumento> tipoDocumentos() {

		return tipoDocumentoRepository.findAll(Sort.by(Sort.Direction.ASC, "tipo"));
	}

	@Override
	public List<TipoDocumentoPlan> tipoDocumentosPlan() {

		return tipoDocumentoPlanRepository.findAll(Sort.by(Sort.Direction.ASC, "tipo"));
	}

	@Override
	public List<Documento> obtenerDocumentosTrabajador(Long idCiudadano) {

		return documentoRepository.findAllByCiudadanoIdCiudadanoAndDeletedFalse(idCiudadano);
	}

	@Override
	public List<GeneraContratoResponseDTO> generarPresentacion(List<GeneraPresentacionDTO> trabajadores) {
		List<GeneraContratoResponseDTO> listaPresentacionesGeneradas = new ArrayList<>();
		PlanConfig config = planConfigService.obtenerConfig(planservice.getPlanActivo().getIdPlan());
		try {
			// carga el fichero de la plantilla de resources
			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaPresentacion);
			File plantilla = classPahtResource.getFile();

			for (GeneraPresentacionDTO generaPresentacionDTO : trabajadores) {

				// Datos de la plantilla
				Presentacion presentacion = presentacionRepository.findById(generaPresentacionDTO.getIdPresentacion())
						.orElseThrow(() -> new PresentacionNotFoundException());

				// Carga el trabajador
				Ciudadano trabajador = ciudadanoRepository.findById(generaPresentacionDTO.getId())
						.orElseThrow(() -> new CiudadanoNotFoundException(generaPresentacionDTO.getId()));
				if (trabajador.getContrato() == null)
					continue;
				// extrae el contrato del trabajador
				Contrato contrato = trabajador.getContrato();

				// carga la plantilla como pdf
				PDDocument nuevoContrato = PDDocument.load(plantilla);
				nuevoContrato.setAllSecurityToBeRemoved(true);
				// obtiene el formulario del documento
				PDAcroForm formulario = nuevoContrato.getDocumentCatalog().getAcroForm();

				// FORMATEO DE FECHAS

				@SuppressWarnings("deprecation")
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", new Locale("es", "ES"));
				String fechaInicio = "00/00/0000";
				String fechaFinal = "00/00/000";
				if (contrato.getFechaInicio() != null && contrato.getFechaFinal() != null) {
					fechaInicio = contrato.getFechaInicio()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
					fechaFinal = contrato.getFechaFinal()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
				}
				formulario.getField("turno").setValue((contrato.getTurno() != null)?contrato.getTurno():"");
				formulario.getField("responsable").setValue(presentacion.getResponsable());
				formulario.getField("nombre").setValue(trabajador.getNombre());
				formulario.getField("apellidos").setValue(trabajador.getApellido1() + " " + trabajador.getApellido2());
				formulario.getField("DNI").setValue(trabajador.getDNI());
				formulario.getField("fechaInicio").setValue(fechaInicio);
				formulario.getField("fechaBaja").setValue(fechaFinal);
				formulario.getField("vacaciones").setValue(presentacion.getVacaciones());
				formulario.getField("observaciones").setValue(presentacion.getObservaciones());
				formulario.getField("categoria").setValue(contrato.getOcupacion().getOcupacion());
				String destino = (contrato.getDestino() != null) ? contrato.getDestino().getDestino() : "";
				formulario.getField("destino")
						.setValue(contrato.getEntidad().getNombreCortoOrganismo() + " / " + destino);

				formulario.flatten();

				// nombre del fichero
				String nombreFichero = trabajador.getApellido1() + "_" + trabajador.getApellido2() + "_"
						+ trabajador.getNombre() + "_" + trabajador.getDNI() + "_PRESENTACION.pdf";

				// carpeta
				// ocupacion del ciudadano
				Ocupacion ocupacionCiudadano = trabajador.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_").replace("/", "_") + "\\";
				// estado
				String estado = trabajador.getEstado().replace("/", "_") + "\\";
				// forma el nombre de la capeta con apellidos_nombre
				String nombreCarpeta = estado + ocupacion + trabajador.getApellido1().replace(" ", "_") + "_"
						+ trabajador.getApellido2().replace(" ", "_") + "_" + trabajador.getNombre().replace(" ", "_");
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(config.getUploadDir() + nombreCarpeta).toAbsolutePath()
						.normalize();
				// log.info(fileStorageLocation.toString());
				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}
				Path fichero = Paths.get(config.getUploadDir() + nombreCarpeta + "\\" + nombreFichero).toAbsolutePath()
						.normalize();
				String contratoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					nombreFichero = nombreFichero.replace("_PRESENTACION", "_" + Instant.now().toEpochMilli() + "_");
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				} else {
					contratoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				}
				;

				nuevoContrato.save(contratoParaGuardar);
				nuevoContrato.close();
				String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
						.path(nombreFichero).toUriString();

				Documento documento = new Documento();
				documento.setIdPlan(planService.getPlanActivo());
				documento.setCiudadano(trabajador);
				documento.setNombre(nombreFichero);
				documento.setRuta(fileDownladUri);
				documento.setTipo("PRESENTACION");

				GeneraContratoResponseDTO response = new GeneraContratoResponseDTO();
				response.setIdCiudadano(trabajador.getIdCiudadano());
				response.setNombre(trabajador.getNombre());
				response.setApellido1(trabajador.getApellido1());
				response.setApellido2(trabajador.getApellido2());
				response.setDNI(trabajador.getDNI());
				response.setDocumento(guardarBBDD(documento));

				listaPresentacionesGeneradas.add(response);

				trabajador.getDocumentos().add(documento);
				ciudadanoService.crearCiudadano(trabajador);

			}

		} catch (Exception e) {
			log.warning(e.getMessage());
			e.printStackTrace();
			throw new DocumentCreationException(e.getMessage());
		}
		return listaPresentacionesGeneradas;
	}

}
