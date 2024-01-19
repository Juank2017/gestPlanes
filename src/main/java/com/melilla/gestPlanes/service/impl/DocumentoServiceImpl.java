package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Presentacion;
import com.melilla.gestPlanes.model.TipoDocumento;
import com.melilla.gestPlanes.repository.DocumentoRepository;
import com.melilla.gestPlanes.repository.DocumentoSpecificationBuilder;
import com.melilla.gestPlanes.repository.PresentacionRepository;
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
	private DocumentoRepository documentoRepository;

	@Autowired
	private TipoDocumentoRepository tipoDocumentoRepository;

	@Autowired
	private PresentacionRepository presentacionRepository;

	@Autowired
	PlanService planService;

	@Value("${file.upload-dir}")
	private String uploadDir;

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

		// Obtiene el ciudadano
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		estado = ciudadano.getEstado().replace("/", "_") + "\\";

		if (ciudadano.getContrato() != null) {
			// ocupacion del ciudadano
			Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
			ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
			// forma el nombre de la capeta con apellidos_nombre
			nombreCarpeta = estado + ocupacion + ciudadano.getApellido1() + "_" + ciudadano.getApellido2() + "_"
					+ ciudadano.getNombre() + "\\" + tipo;
		} else {

			nombreCarpeta = estado + ciudadano.getApellido1() + "_" + ciudadano.getApellido2() + "_"
					+ ciudadano.getNombre() + "\\" + tipo;
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
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
					.path(fileName).toUriString();

			Documento documento = new Documento();

			documento.setCiudadano(ciudadano);
			documento.setNombre(fileName);
			documento.setRuta(fileDownladUri);
			documento.setIdPlan(planService.getPlanActivo());
			documento.setTipo(tipo);
			// documentoRepository.save(documento);
			return documento;
		} catch (IOException ex) {
			throw new FileStorageException("No se pudo subir el documento " + fileName + ". Intentelo de nuevo!");
		}

	}

	@Override
	public Resource loadDocumentAsResource(Long idCiudadano, String filename, Long idDocumento) {
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		String estado = null;
		Documento doc = documentoRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		estado = ciudadano.getEstado().replace("/", "_") + "\\";
		Ocupacion ocupacionCiudadano = ciudadano.getContrato().getOcupacion();
		String ocupacion = ocupacionCiudadano.getOcupacion().replace(" ", "_") + "\\";
		String nombreCarpeta = estado + ocupacion + ciudadano.getApellido1() + "_" + ciudadano.getApellido2() + "_"
				+ ciudadano.getNombre() + "\\" + doc.getTipo() + "\\";
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
	public String eliminarDocumento(Long idCiudadano, String nombreDocumento) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Documento guardarBBDD(Documento documento) {

		return documentoRepository.save(documento);
	}

	@Override
	public List<GeneraContratoResponseDTO> generarContrato(List<GeneraContratoDTO> trabajadores) {

		List<GeneraContratoResponseDTO> listaContratosGenerados = new ArrayList<>();

		try {
			// carga el fichero de la plantilla de resources
			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaContrato);
			File plantilla = classPahtResource.getFile();

			for (GeneraContratoDTO generaContratoDTO : trabajadores) {

				// Carga el trabajador
				Ciudadano trabajador = ciudadanoService.getTrabajadorPorDNI(generaContratoDTO.getIdent())
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

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu", new Locale("es", "ES"));

				String fechaNacimiento = trabajador.getFechaNacimiento()
						.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
				String mesFechaFirma="00/00/0000";
				String fechaInicio = "00/00/0000";
				String fechaFinal = "00/00/000";
				if (contrato.getFechaInicio() != null && contrato.getFechaFinal() != null) {
					fechaInicio = contrato.getFechaInicio()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
					fechaFinal = contrato.getFechaFinal()
							.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES")));
					 mesFechaFirma = contrato.getFechaInicio()
							.format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")));

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
				formulario.getField("AA0407").setValue("ESPAÑOLA");
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
				formulario.getField("C303").setValue("UN MES");

				formulario.getField("C401").setValue(contrato.getTotal());
				formulario.getField("C402").setValue("MENSUALES");
				formulario.getField("C403").setValue("Salario base: " + contrato.getBase() + "€, prorata extra: "
						+ contrato.getProrratas() + "€, residencia: " + contrato.getResidencia() + "€");

				formulario.getField("C501").setValue("30 DÍAS ANUALES");

				formulario.getField("C801").setValue("MELILLA");

				formulario.getField("P11BO1").setValue("Elección2");
				formulario.getField("P11BO2").setValue("Elección2");
				formulario.getField("P1108").setValue(
						"Programa de colaboración con órganos de la Administración General del Estado que contraten trabajadores desempleados para la realización de obras y servicios de interés general y social en el ámbito de la Orden del Ministerios de Trabajo y Asuntos Sociales de 19 de diciembre de 1997, modificada por la Orden TAS/2435/2004, de 20 de julio y por la Orden ESS 974/2013, de 20 de mayo, establece las bases reguladoras de la concesión de subvenciones públicas por los Servicios Públicos de Empleo en el ámbito de la colaboración con órganos de la Administración General del Estado y sus organismos autónomos, universidades públicas e instituciones sin ánimo de lucro, que contraten trabajadores desempleados para la realización de obras y servicios de interés general y social.");

				// Literal contrato
				formulario.getField("P2301").setValue(contrato.getEntidad().getLiteralContrato());

				formulario.getField("P2302").setValue("MELILLA");

				formulario.getField("P2303").setValue("" + contrato.getFechaInicio().getDayOfMonth());
				formulario.getField("P2304").setValue(mesFechaFirma.toUpperCase());
				formulario.getField("P2305").setValue("" + contrato.getFechaInicio().getYear());

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
						+ trabajador.getNombre() + "\\CONTRATO";
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
				nuevoContrato.save(fileStorageLocation + "\\" + nombreFichero);
				nuevoContrato.close();
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
	public List<TipoDocumento> tipoDocumentos() {

		return tipoDocumentoRepository.findAll(Sort.by(Sort.Direction.ASC, "tipo"));
	}

	@Override
	public List<Documento> obtenerDocumentosTrabajador(Long idCiudadano) {

		return documentoRepository.findAllByCiudadanoIdCiudadano(idCiudadano);
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
				Ciudadano trabajador = ciudadanoService.getTrabajadorPorDNI(generaPresentacionDTO.getIdent())
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
				formulario.getField("categoria").setValue(contrato.getCategoria().getCategoria());
				formulario.getField("destino").setValue(contrato.getDestino().getDestino());

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
						+ trabajador.getNombre() + "\\PRESENTACION";
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
				nuevoContrato.save(fileStorageLocation + "\\" + nombreFichero);
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
