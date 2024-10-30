package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.NumberingFormat;
import com.deepoove.poi.data.NumberingItemRenderData;
import com.deepoove.poi.data.NumberingRenderData;
import com.deepoove.poi.data.Numberings;
import com.deepoove.poi.data.Numberings.NumberingBuilder;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.Texts;
import com.deepoove.poi.data.style.Style;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.melilla.gestPlanes.DTO.DocumentoAZip;
import com.melilla.gestPlanes.DTO.DocumentoProcedimientoAZip;
import com.melilla.gestPlanes.DTO.GeneraAcuerdoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentoNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.MyFileNotFoundException;
import com.melilla.gestPlanes.exceptions.exceptions.PdfConvertionException;
import com.melilla.gestPlanes.exceptions.exceptions.ProcedimientoSinPeriodosException;
import com.melilla.gestPlanes.model.Abogado;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoProcedimientoReclamacion;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.repository.DocumentoProcedimientoReclamacionRepository;
import com.melilla.gestPlanes.service.DocumentosProcedimientoService;
import com.melilla.gestPlanes.service.ProcedimientoService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class DocumentosProcedimientoServiceImpl implements DocumentosProcedimientoService {

	@Autowired
	ProcedimientoService procedimientoService;
	
	@Autowired
	DocumentoProcedimientoReclamacionRepository docRepository;

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${file.acuerdo}")
	private String plantillaAcuerdo;

	@Value("${file.acuerdoWORD}")
	private String plantillaAcuerdoWORD;

	@Value("${file.email}")
	private String email;

	@Value("${file.upload-dir}")
	private String uploadDir;

	@Value("${file.temp-dir}")
	private String tempDir;

	@Override
	public void generaAcuerdo(List<GeneraAcuerdoDTO> acuerdos) {

		try {
			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaAcuerdo);
			File plantilla = classPahtResource.getFile();

			for (GeneraAcuerdoDTO generaAcuerdoDTO : acuerdos) {

				Procedimiento procedimiento = procedimientoService.getProcedimiento(generaAcuerdoDTO.getId());

				List<ContratoReclamado> periodos = procedimiento.getPeriodos();

				if (periodos.isEmpty())
					continue;

				// carga la plantilla como pdf
				PDDocument nuevoAcuerdo = PDDocument.load(plantilla);
				nuevoAcuerdo.setAllSecurityToBeRemoved(true);
				// obtiene el formulario del documento
				PDAcroForm formulario = nuevoAcuerdo.getDocumentCatalog().getAcroForm();

				// Rellena la cabecera del contrato

				// formulario.getField("AA0101-DNI").setValue("S2916002E");

				PDTextField field = (PDTextField) formulario.getField("AA0101-DNI");

				Resource fuente = resourceLoader.getResource("classpath:Arial-BoldMT.ttf");
				PDFont font = PDType0Font.load(nuevoAcuerdo, fuente.getInputStream(), false);

				PDResources resources = new PDResources();
				resources.add(font);

				formulario.setDefaultResources(resources);
				// resources.getFontNames().forEach((f) -> log.warning(f.toString()));
				String defaultAppearanceString = "/F1 0 Tf 0 g";
				field.setDefaultAppearance(defaultAppearanceString);

				formulario.getFields().forEach((f) -> {
					if (f instanceof PDTextField) {
						((PDTextField) f).setDefaultAppearance(defaultAppearanceString);
					}
				});

				String fechaSentencia = null;
				if (procedimiento.getFechaSentencia() != null) {
					fechaSentencia = procedimiento.getFechaSentencia()
							.format(DateTimeFormatter.ofPattern("dd de L de uuuu", new Locale("es", "ES")));
				} else {
					throw new DocumentCreationException("La fecha de sentencia no puede ser nula");
				}

				formulario.getField("procedimiento").setValue(procedimiento.getNumeroProcedimiento());
				formulario.getField("fecha").setValue(fechaSentencia);

				BigDecimal cantidad = procedimientoService
						.totalReconocidoProcedimiento(procedimiento.getIdProcedimiento());

				formulario.getField("cantidad").setValue(cantidad.toString());

				String trabajador = procedimiento.getCiudadano().getNombre() + " "
						+ procedimiento.getCiudadano().getApellido1() + " "
						+ procedimiento.getCiudadano().getApellido2();

				formulario.getField("trabajador").setValue(trabajador);

				formulario.getField("dni").setValue(procedimiento.getCiudadano().getDNI());

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public List<DocumentoProcedimientoAZip> generaAcuerdoWord(List<GeneraAcuerdoDTO> acuerdos) {
		
		List<DocumentoProcedimientoReclamacion> documentosGenerados= new ArrayList<DocumentoProcedimientoReclamacion>();
		List<DocumentoProcedimientoAZip> documentosZIP = new ArrayList<DocumentoProcedimientoAZip>();

		Path fileStorageLocation=null;
		try {

			Resource classPahtResource = resourceLoader.getResource("classpath:" + plantillaAcuerdoWORD);
			File plantilla = classPahtResource.getFile();

			for (GeneraAcuerdoDTO generaAcuerdoDTO : acuerdos) {

				Procedimiento procedimiento = procedimientoService.getProcedimiento(generaAcuerdoDTO.getId());

				List<ContratoReclamado> periodos = procedimiento.getPeriodos();

				if (periodos.isEmpty())
					continue;

				Ciudadano ciudadano = procedimiento.getCiudadano();

				String trabajador = procedimiento.getCiudadano().getNombre() + " "
						+ procedimiento.getCiudadano().getApellido1() + " "
						+ procedimiento.getCiudadano().getApellido2();

				// Abogado abogado = procedimiento.getAbogado();

				BigDecimal cantidad = procedimientoService
						.totalReconocidoProcedimiento(procedimiento.getIdProcedimiento());

				Map<String, Object> datos = new HashMap<String, Object>();

				datos.put("procedimiento", Texts.of(procedimiento.getNumeroProcedimiento()).bold().create());
				datos.put("fecha",
						Texts.of(procedimiento.getFechaSentencia()
								.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES"))).toString())
								.bold().create());
				datos.put("cantidad", Texts.of(cantidad.toString()).bold().create());
				datos.put("trabajador", Texts.of(trabajador).bold().create());
				datos.put("dni", Texts.of(ciudadano.getDNI()).bold().create());

				NumberingBuilder builder = Numberings.of(NumberingFormat.DECIMAL);

				for (ContratoReclamado periodo : periodos) {

					TextRenderData texto = new TextRenderData();
					texto.setText("\tDel "
							+ periodo.getFechaInicio()
									.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES"))).toString()
							+ " al "
							+ periodo.getFechaFinal()
									.format(DateTimeFormatter.ofPattern("dd/MM/uuu", new Locale("es", "ES"))).toString()
							+ " grupo de cotización " + periodo.getGc());

					texto.setStyle(Style.builder().buildBold().build());

					builder.addItem(texto);

				}

				NumberingRenderData lista = builder.create();

				datos.put("contratos", lista); 

				String numeroProcedimiento = procedimiento.getNumeroProcedimiento().replace("/", "_");

				log.info(numeroProcedimiento);

				// nombre del fichero
				String nombreFichero = numeroProcedimiento + "_ACUERDO_" + ciudadano.getApellido1() + "_"
						+ ciudadano.getApellido2() + "_" + ciudadano.getNombre() + "_" + ciudadano.getDNI() + ".docx";

				String nombreFicheroPDF = numeroProcedimiento + "_ACUERDO_" + ciudadano.getApellido1() + "_"
						+ ciudadano.getApellido2() + "_" + ciudadano.getNombre() + "_" + ciudadano.getDNI() + ".pdf";

				String nombreCarpeta = "reclamaciones\\" + numeroProcedimiento;
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();

			
				Files.createDirectories(fileStorageLocation);
			

				Path fichero = Paths.get(uploadDir + nombreCarpeta + "\\" + nombreFichero).toAbsolutePath().normalize();
				String acuerdoParaGuardar;
				String acuerdoParaGuardarPDF;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					nombreFichero = nombreFichero.replace("_ACUERDO", "_" + Instant.now().toEpochMilli() + "_");
					nombreFicheroPDF = nombreFicheroPDF.replace("_ACUERDO", "_" + Instant.now().toEpochMilli() + "_");
					acuerdoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
					acuerdoParaGuardarPDF = fileStorageLocation + "\\" + nombreFicheroPDF;
				} else {
					acuerdoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
					acuerdoParaGuardarPDF = fileStorageLocation + "\\" + nombreFicheroPDF;
				}

				String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
						.path(nombreFichero).toUriString();

				DocumentoProcedimientoReclamacion documento = new DocumentoProcedimientoReclamacion();

				documento.setProcedimiento(procedimiento);
				documento.setNombre(nombreFicheroPDF);
				documento.setRuta(fileDownladUri);
				documento.setTipo("ACUERDO");
				documento = docRepository.save(documento);
				documentosGenerados.add(documento);
				
				DocumentoProcedimientoAZip docAzip = new DocumentoProcedimientoAZip();
				
				docAzip.setIdDocumento(documento.getIdDocumentoProcedimiento());
				docAzip.setIdProcedimiento(procedimiento.getIdProcedimiento());

				documentosZIP.add(docAzip);

				XWPFTemplate.compile(plantilla).render(datos).writeToFile(acuerdoParaGuardar);

				InputStream word = new FileInputStream(acuerdoParaGuardar);

				OutputStream pdf = new FileOutputStream(acuerdoParaGuardarPDF);

				log.info(acuerdoParaGuardar);

				log.info(acuerdoParaGuardarPDF);

				IConverter converter = LocalConverter.builder().baseFolder(new File(tempDir))
						.workerPool(20, 25, 2, TimeUnit.SECONDS).processTimeout(5, TimeUnit.SECONDS).build();

				Future<Boolean> conversion = converter.convert(word).as(DocumentType.MS_WORD).to(pdf)
						.as(DocumentType.PDF).prioritizeWith(1000) // optional
						.schedule();

				while(!conversion.isDone()) {
					log.info("convirtiendo");
				}
			
			}
			
		
			return documentosZIP;
			
		} catch (IOException e) {
			throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new PdfConvertionException("No se ha podido generar el pdf. "+ e.getMessage());
		}

	}
	
	public Resource loadDocumentAsResource(Long idProcedimiento, String filename, Long idDocumento) {
	Procedimiento procedimiento = procedimientoService.getProcedimiento(idProcedimiento);
	Ciudadano ciudadano = procedimiento.getCiudadano();
		String estado = null;
		String apellido="_";
		DocumentoProcedimientoReclamacion doc = docRepository.findById(idDocumento)
				.orElseThrow(() -> new DocumentoNotFoundException(idDocumento));
		//obtiene el apellido y sustituye los espacios por _
		apellido =(ciudadano.getApellido1()!= null)? ciudadano.getApellido1().replace(" ","_"):"null";
		
		String nProcedimiento = procedimiento.getNumeroProcedimiento().replace('/', '_');
		
		String nombreCarpeta = "reclamaciones\\" +nProcedimiento+"\\";
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
	public void downloadDocumentsAsZipFile(HttpServletResponse response, List<DocumentoProcedimientoAZip> docs) {
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=download.zip");
		try {

			ZipOutputStream zipOutput = new ZipOutputStream(response.getOutputStream());

			for (DocumentoProcedimientoAZip documentoAZip : docs) {

				DocumentoProcedimientoReclamacion documento = docRepository.findById(documentoAZip.getIdDocumento())
						.orElseThrow(() -> new DocumentoNotFoundException(documentoAZip.getIdDocumento()));

				Resource ficheroAComprimir = loadDocumentAsResource(documentoAZip.getIdProcedimiento(),
						documento.getNombre(), documento.getIdDocumentoProcedimiento());

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
	public void eliminaDocumentoProcedimiento(long idDocumento) {
		
		docRepository.deleteById(idDocumento);
		
	}
}
