package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.melilla.gestPlanes.exceptions.exceptions.PresentacionNotFoundException;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Presentacion;
import com.melilla.gestPlanes.model.TipoDocumento;
import com.melilla.gestPlanes.model.TipoDocumentoPlan;
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
		String apellido="_";

		// Obtiene el ciudadano
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		estado = ciudadano.getEstado().replace("/", "_") + "\\";
		
		switch (estado) {
		case "FINALIZADO_A\\" : 
			log.warning("case: "+estado);
			estado = "CONTRATADO_A\\";
			break;
		case "DESPEDIDO_A\\": 
			
			estado= "CONTRATADO_A\\";
			break;

		case "RENUNCIA\\": 
			
			estado= "CONTRATADO_A\\";
			break;
		
		}
		log.warning(estado);
		if (ciudadano.getContrato() != null) {
			// ocupacion del ciudadano
			Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
			ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
			//obtiene el apellido y sustituye los espacios por _
			apellido = ciudadano.getApellido1().replace(" ","_");
			// forma el nombre de la capeta con apellidos_nombre
			nombreCarpeta = estado + ocupacion + apellido  + "_" + ciudadano.getApellido2() + "_"
					+ ciudadano.getNombre();
		} else {

			nombreCarpeta = estado + apellido + "_" + ciudadano.getApellido2() + "_"
					+ ciudadano.getNombre();
		}

		// obtiene el path absoluto debe ser S:\PLANES DE
		// EMPLEO\ocupacion\apellidos_nombre
		Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();
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
		}catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo "+ fileName +" ya existe");
		}
		catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}

	}

	@Override
	public DocumentoPlan guardarDocumentoPlan(Long idPlan, MultipartFile file, String tipo) {

		
		String nombreCarpeta= "PLAN";


		// obtiene el path absoluto debe ser S:\PLANES DE
		// EMPLEO\ocupacion\apellidos_nombre
		nombreCarpeta= nombreCarpeta+"\\" + tipo;
		Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();
		log.info(fileStorageLocation.toString());
		// Intenta crear el directorio si no existe.
		try {
			Files.createDirectories(fileStorageLocation);
		} catch (Exception e) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		}

		// nombre del fichero
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		//fileName = tipo + "_" + fileName;
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
		}catch (FileAlreadyExistsException e) {
			throw new FileStorageException("El archivo "+ fileName +" ya existe");
		}
		catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}

	}
	@Override
	public Resource loadDocumentAsResource(Long idCiudadano, String filename, Long idDocumento) {
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		String estado = null;
		String apellido="_";
		Documento doc = documentoRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		//obtiene el apellido y sustituye los espacios por _
		apellido =(ciudadano.getApellido1()!= null)? ciudadano.getApellido1().replace(" ","_"):"null";
		estado = ciudadano.getEstado().replace("/", "_") + "\\";
		Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
		String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
		String nombreCarpeta = estado + ocupacion + ciudadano.getApellido1() + "_" + ciudadano.getApellido2() + "_"
				+ ciudadano.getNombre()+"\\";
		try {
			Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta + filename).toAbsolutePath().normalize();
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
	public Resource loadDocumentPlanAsResource( String filename, Long idDocumento) {
		
		DocumentoPlan doc = documentoPlanRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		
		String nombreCarpeta ="PLAN\\" + doc.getTipo() + "\\";
		try {
			Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta + filename).toAbsolutePath().normalize();
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
		
		String estado = null;
		String apellido="_";
		Documento doc = documentoRepository.findById(idDocumento).orElseThrow(()-> new DocumentoNotFoundException(idDocumento));
		String filename = doc.getNombre();
		Ciudadano ciudadano = doc.getCiudadano();
		//obtiene el apellido y sustituye los espacios por _
				apellido = ciudadano.getApellido1().replace(" ","_");
				estado = ciudadano.getEstado().replace("/", "_") + "\\";
				Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
				String nombreCarpeta = estado + ocupacion + ciudadano.getApellido1() + "_" + ciudadano.getApellido2() + "_"
						+ ciudadano.getNombre()+"\\";
				try {
					Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta + filename).toAbsolutePath().normalize();
					log.info(fileStorageLocation.toString());
					log.info(fileStorageLocation.toUri().toString());
					Resource resource = new UrlResource(fileStorageLocation.toUri());

					if (resource.exists()) {
						File fichero = resource.getFile();
						Path fileTrashcanLocartion= Paths.get(trashcanDir + Instant.now().toEpochMilli()+"_" + fichero.getName()).toAbsolutePath().normalize();
						 Files.move(fileStorageLocation, fileTrashcanLocartion, StandardCopyOption.REPLACE_EXISTING);
						 documentoRepository.deleteById(idDocumento);
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
	public void eliminarDocumentoPlan(Long idDocumento) {
		
		String nombreCarpeta= "PLAN";
		DocumentoPlan doc = documentoPlanRepository.findById(idDocumento).orElseThrow(()-> new DocumentoNotFoundException(idDocumento));
		String filename = doc.getNombre();
		
		
				 nombreCarpeta =nombreCarpeta+"\\"+  doc.getTipo() + "\\";
				try {
					Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta + filename).toAbsolutePath().normalize();
					log.info(fileStorageLocation.toString());
					log.info(fileStorageLocation.toUri().toString());
					Resource resource = new UrlResource(fileStorageLocation.toUri());

					if (resource.exists()) {
						File fichero = resource.getFile();
						Path fileTrashcanLocartion= Paths.get(trashcanDir + Instant.now().toEpochMilli()+"_" + fichero.getName()).toAbsolutePath().normalize();
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

		List<GeneraContratoResponseDTO> listaContratosGenerados = new ArrayList<>();

		try {
			log.warning("Inicia genera contrato");
			// carga el fichero de la plantilla de resources

			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaContrato);
			File plantilla = classPahtResource.getFile();
			for (GeneraContratoDTO generaContratoDTO : trabajadores) {
				log.warning("Inicio Genera contrato: "+generaContratoDTO.getId());
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

				@SuppressWarnings("deprecation")
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", new Locale("es", "ES"));

				String fechaNacimiento= null;
				if(trabajador.getFechaNacimiento() != null) {
					 fechaNacimiento = trabajador.getFechaNacimiento()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
				}else {
					throw new DocumentCreationException("La fecha de nacimiento no puede ser nula");
				}

				String mesFechaFirma="00/00/0000";
				String fechaInicio = "00/00/0000";
				String fechaFinal = "00/00/0000";
				if (contrato.getFechaInicio() != null && contrato.getFechaFinal() != null) {
					fechaInicio = contrato.getFechaInicio()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
					fechaFinal = contrato.getFechaFinal()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
					 mesFechaFirma = contrato.getFechaInicio()
							.format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

				}else {
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

				formulario.getField("AA0401").setValue(
						trabajador.getNombre() + " " + trabajador.getApellido1() + " " + trabajador.getApellido2());
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
				
				if(!trabajador.isSinClausula()) {
					formulario.getField("C303").setValue("UN MES");	
				}else {
					formulario.getField("C303").setValue("SIN PERIODO DE PRUEBA");
				}
				

				formulario.getField("C401").setValue(contrato.getTotal());
				formulario.getField("C402").setValue("MENSUALES");
				formulario.getField("C403").setValue(" SALARIO BASE + P.P.P.E + INDEMNIZACIÓN DE RESIDENCIA");

				formulario.getField("C501").setValue("30 DÍAS NATURALES");

				formulario.getField("C801").setValue("MELILLA");

				formulario.getField("P11CV1").setValue("Sí");
				formulario.getField("P11BO1").setValue("Elección2");
				formulario.getField("P11BO2").setValue("Elección2");
				formulario.getField("P1108").setValue(
						"Programa común de inserción laboral a través de obras y servicios de interés general y social, recogido en la subsección 1ª de la sección 3ª del Capitulo V del Real Decreto 818/2021 de 28 de septiembre y la orden TES/1077/2023  de 28 de septiembre y la convocatoria para la concesión de subvenciones destinadas al anterior programa en colaboración con órganos de la AGE en el ámbito territorial de las ciudades de Ceuta y Melilla, aprobada por resolución de 7/11/2023 de la Dirección General del SEPE.");

				// Literal contrato
				formulario.getField("P2301").setValue("El presente contrato se formaliza para participar en los programas, o en su caso programa, contenidos en el documento de colaboración formalizado entre la Delegación del Gobierno y la entidad donde va a desarrollar la actividad laboral el trabajador contratado, para el desarrollo del Plan de Empleo 2023-2024.");

				formulario.getField("P2302").setValue("MELILLA");

				formulario.getField("P2303").setValue("" + contrato.getFechaInicio().getDayOfMonth());
				formulario.getField("P2304").setValue(mesFechaFirma.toUpperCase());
				formulario.getField("P2305").setValue("" + contrato.getFechaInicio().getYear());
				
				formulario.flatten();

				// nombre del fichero
				String nombreFichero = trabajador.getApellido1() + "_" + trabajador.getApellido2() + "_"
						+ trabajador.getNombre() + "_" + trabajador.getDNI() + "_CONTRATO.pdf";

				// carpeta
				// ocupacion del ciudadano
				Ocupacion ocupacionCiudadano = trabajador.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
				//estado
				String estado = trabajador.getEstado().replace("/", "_") +"\\";
				// forma el nombre de la capeta con apellidos_nombre
				String nombreCarpeta = estado + ocupacion + trabajador.getApellido1() + "_" + trabajador.getApellido2() + "_"
						+ trabajador.getNombre();
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();
				 log.info(fileStorageLocation.toString());
				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}
				
				Path fichero = Paths.get(uploadDir + nombreCarpeta+ "\\" + nombreFichero).toAbsolutePath().normalize();
				String contratoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					nombreFichero = nombreFichero.replace("_CONTRATO","_"+Instant.now().toEpochMilli() +"_" );
					contratoParaGuardar= fileStorageLocation + "\\" + nombreFichero;
				}else {
					contratoParaGuardar= fileStorageLocation + "\\" +nombreFichero;
				};
				log.warning("Genera contrato guardando el pdf a disco: "+generaContratoDTO.getId());
				nuevoContrato.save(contratoParaGuardar);
				nuevoContrato.close();
				log.warning("Genera contrato guardado el pdf a disco: "+generaContratoDTO.getId());
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
				log.warning("fin Genera contrato: "+generaContratoDTO.getId());
			}

		} catch (Exception e) {
			log.warning(e.getMessage());
			e.printStackTrace();
			throw new DocumentCreationException(e.getMessage());
		}
		return listaContratosGenerados;

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

				Resource ficheroAComprimir = loadDocumentPlanAsResource(
						documento.getNombre(), documento.getIdDocumentoPlan());

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

		//List<DocumentoPlan> response = new ArrayList<DocumentoPlan>();

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

				formulario.getField("responsable").setValue(presentacion.getResponsable());
				formulario.getField("nombre").setValue(trabajador.getNombre());
				formulario.getField("apellidos").setValue(trabajador.getApellido1() + " " + trabajador.getApellido2());
				formulario.getField("DNI").setValue(trabajador.getDNI());
				formulario.getField("fechaInicio").setValue(fechaInicio);
				formulario.getField("fechaBaja").setValue(fechaFinal);
				formulario.getField("vacaciones").setValue(presentacion.getVacaciones());
				formulario.getField("observaciones").setValue(presentacion.getObservaciones());
				formulario.getField("categoria").setValue(contrato.getOcupacion().getOcupacion());
				formulario.getField("destino").setValue(contrato.getEntidad().getNombreCortoOrganismo()+ " / " + contrato.getDestino().getDestino());
				
				formulario.flatten();

				// nombre del fichero
				String nombreFichero = trabajador.getApellido1() + "_" + trabajador.getApellido2() + "_"
						+ trabajador.getNombre() + "_" + trabajador.getDNI() + "_PRESENTACION.pdf";

				// carpeta
				// ocupacion del ciudadano
				Ocupacion ocupacionCiudadano = trabajador.getContrato().getOcupacion();
				String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
				//estado
				String estado = trabajador.getEstado().replace("/", "_") +"\\";
				// forma el nombre de la capeta con apellidos_nombre
				String nombreCarpeta = estado + ocupacion + trabajador.getApellido1() + "_" + trabajador.getApellido2() + "_"
						+ trabajador.getNombre();
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();
				// log.info(fileStorageLocation.toString());
				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}
				Path fichero = Paths.get(uploadDir + nombreCarpeta+ "\\" + nombreFichero).toAbsolutePath().normalize();
				String contratoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					nombreFichero = nombreFichero.replace("_PRESENTACION","_"+Instant.now().toEpochMilli() +"_" );
					contratoParaGuardar= fileStorageLocation + "\\" + nombreFichero;
				}else {
					contratoParaGuardar= fileStorageLocation + "\\" +nombreFichero;
				};
				
				
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
