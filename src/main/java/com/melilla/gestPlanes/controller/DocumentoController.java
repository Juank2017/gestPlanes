package com.melilla.gestPlanes.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.melilla.gestPlanes.DTO.DocumentoAZip;
import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.DTO.GeneraContratoDTO;
import com.melilla.gestPlanes.DTO.GeneraPresentacionDTO;
import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.model.DocumentoResponse;
import com.melilla.gestPlanes.service.DocumentoService;
import com.melilla.gestPlanes.service.PlanService;
import com.melilla.gestPlanes.service.TipoDocumentoPlanService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@RestController
@Log
public class DocumentoController {

	@Autowired
	private DocumentoService documentoService;
	
	
	
	@Autowired
	private PlanService planService;

	@PostMapping("/subirDocumento")
	public ResponseEntity<ApiResponse> subirDocumento(@RequestPart MultipartFile file, @RequestPart String tipo,
			@RequestPart String idCiudadano) {

		ApiResponse response = new ApiResponse();

		Documento doc = documentoService.guardarDocumento(Long.parseLong(idCiudadano), file, tipo);

		response.setEstado(HttpStatus.OK);
		response.getPayload().add(documentoService.guardarBBDD(doc));
		response.setMensaje("Lista de ciudadanos");

		return ResponseEntity.ok(response);

	}
	
	@PostMapping("/subirDocumentoPlan" )
	public ResponseEntity<ApiResponse> subirDocumentoPlan(@RequestPart MultipartFile file, @RequestPart String tipo,
			@RequestPart Long idPlan) {

		ApiResponse response = new ApiResponse();

		

		response.setEstado(HttpStatus.OK);
		response.getPayload().add(documentoService.guardarDocumentoPlan(planService.getPlanActivo().getIdPlan(), file, tipo));
		response.setMensaje("Lista de ciudadanos");

		return ResponseEntity.ok(response);

	}

	@GetMapping("/descargaDocumento/{fileName:.+}/{idCiudadano}/{idDocumento}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, @PathVariable Long idCiudadano,
			@PathVariable Long idDocumento, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = documentoService.loadDocumentAsResource(idCiudadano, fileName, idDocumento);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			throw new FileStorageException("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PostMapping("/generaContrato")
	ResponseEntity<ApiResponse> generaContrato(@RequestBody List<GeneraContratoDTO> trabajadores) {

		log.warning(trabajadores.toString());
		ApiResponse response = new ApiResponse();

		response.getPayload().add(documentoService.generarContrato(trabajadores));

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/generaPresentacion")
	ResponseEntity<ApiResponse> generaPresentacion(@RequestBody List<GeneraPresentacionDTO> trabajadores) {

		log.warning(trabajadores.toString());
		ApiResponse response = new ApiResponse();

		response.getPayload().add(documentoService.generarPresentacion(trabajadores));

		return ResponseEntity.ok(response);
	}

	@PostMapping("/downloadZip")
	void descargaDocumentosZip(HttpServletResponse response, @RequestBody List<DocumentoAZip> docs) {

		documentoService.downloadDocumentsAsZipFile(response, docs);
	}

	@PostMapping("/buscaDocumentos")
	ResponseEntity<ApiResponse> buscaDocumentos(@RequestBody List<DocumentoCriterioBusqueda> criterios) {

//		DocumentoCriterioBusqueda criterios = new DocumentoCriterioBusqueda();
//
//		if (tipo != null)
//			criterios.setTipo(tipo);
//		if (fechaInicial != null)
//			criterios.setFechaInicial(fechaInicial);
//		if (fechaFinal != null)
//			criterios.setFechaFinal(fechaFinal);
//		if (dni != null)
//			criterios.setDni(dni);

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(documentoService.buscarDocumentos(criterios));
		response.setMensaje("Lista de documentos");

		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/buscaDocumentosPlan")
	ResponseEntity<ApiResponse> buscaDocumentosPlan(@RequestBody List<DocumentoCriterioBusqueda> criterios) {

//		DocumentoCriterioBusqueda criterios = new DocumentoCriterioBusqueda();
//
//		if (tipo != null)
//			criterios.setTipo(tipo);
//		if (fechaInicial != null)
//			criterios.setFechaInicial(fechaInicial);
//		if (fechaFinal != null)
//			criterios.setFechaFinal(fechaFinal);
//		if (dni != null)
//			criterios.setDni(dni);

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(documentoService.buscarDocumentosPlan(criterios));
		response.setMensaje("Lista de documentos");

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/tipoDocumentos")
	ResponseEntity<ApiResponse>tiposDeDocumentos(){
		ApiResponse response= new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(documentoService.tipoDocumentos());
		response.setMensaje("");
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/tipoDocumentosPlan")
	ResponseEntity<ApiResponse>tiposDeDocumentosPlan(){
		ApiResponse response= new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(documentoService.tipoDocumentosPlan());
		response.setMensaje("");
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/documentosTrabajador/{idCiudadano}")
	ResponseEntity<ApiResponse>obtenerDocumentosTrabajador(@PathVariable Long idCiudadano){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(documentoService.obtenerDocumentosTrabajador(idCiudadano));
		response.setMensaje("Lista documentos");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/eliminarDocumentoTrabajador/{idDocumento}")
	ResponseEntity<ApiResponse>eliminarDocumentoTrabajador(@PathVariable Long idDocumento){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		documentoService.eliminarDocumento(idDocumento);
		response.setMensaje("Documento eliminado");
		
		return ResponseEntity.ok(response);
	}

}
