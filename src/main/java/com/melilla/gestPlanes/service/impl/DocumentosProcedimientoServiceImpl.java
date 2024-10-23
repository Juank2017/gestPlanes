package com.melilla.gestPlanes.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import org.springframework.stereotype.Service;
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
import com.melilla.gestPlanes.DTO.GeneraAcuerdoDTO;
import com.melilla.gestPlanes.exceptions.exceptions.DocumentCreationException;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.exceptions.exceptions.ProcedimientoSinPeriodosException;
import com.melilla.gestPlanes.model.Abogado;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.service.DocumentosProcedimientoService;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class DocumentosProcedimientoServiceImpl implements DocumentosProcedimientoService {

	@Autowired
	ProcedimientoService procedimientoService;

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
	public void generaAcuerdoWord(List<GeneraAcuerdoDTO> acuerdos) {

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
					texto.setText("Del "
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

				datos.put("contratos", lista); // .create(contratos.toArray(String[]::new)));

				String numeroProcedimiento = procedimiento.getNumeroProcedimiento().replace("/", "_");

				log.info(numeroProcedimiento);

				// nombre del fichero
				String nombreFichero = numeroProcedimiento + "_" + ciudadano.getApellido1() + "_"
						+ ciudadano.getApellido2() + "_" + ciudadano.getNombre() + "_" + ciudadano.getDNI() + ".docx";

				String nombreCarpeta = "reclamaciones\\" + numeroProcedimiento;
				// obtiene el path absoluto debe ser S:\PLANES DE
				// EMPLEO\ocupacion\apellidos_nombre
				Path fileStorageLocation = Paths.get(uploadDir + nombreCarpeta).toAbsolutePath().normalize();

				// Intenta crear el directorio si no existe.
				try {
					Files.createDirectories(fileStorageLocation);
				} catch (Exception e) {
					throw new FileStorageException("No se ha podido crear el directorio: " + fileStorageLocation);
				}

				Path fichero = Paths.get(uploadDir + nombreCarpeta + "\\" + nombreFichero).toAbsolutePath().normalize();
				String acuerdoParaGuardar;
				if (Files.exists(fichero, LinkOption.NOFOLLOW_LINKS)) {
					nombreFichero = nombreFichero.replace("_CONTRATO", "_" + Instant.now().toEpochMilli() + "_");
					acuerdoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				} else {
					acuerdoParaGuardar = fileStorageLocation + "\\" + nombreFichero;
				}
				;

				String fileDownladUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/descargaDocumento/")
						.path(nombreFichero).toUriString();

				Documento documento = new Documento();

				documento.setCiudadano(ciudadano);
				documento.setNombre(nombreFichero);
				documento.setRuta(fileDownladUri);
				documento.setTipo("ACUERDO");

				XWPFTemplate.compile(plantilla).render(datos).writeToFile(acuerdoParaGuardar);

			}

//			InputStream inputStream = new FileInputStream(plantilla);
//			
//			XWPFDocument doc = new XWPFDocument(inputStream);

		} catch (Exception e) {
			log.info(e.getMessage());
		}

	}

}
