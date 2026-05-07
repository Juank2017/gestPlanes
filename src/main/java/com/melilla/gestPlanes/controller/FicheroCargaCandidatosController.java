package com.melilla.gestPlanes.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.exceptions.exceptions.FileStorageException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.FicheroCargaCandidatosService;

import jakarta.mail.internet.ContentType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FicheroCargaCandidatosController {
	
	@Autowired
	FicheroCargaCandidatosService ficheroCargaCandidatosService;
	
	
	@GetMapping("/cargaCandidatos")
	public ResponseEntity<ApiResponse>obtenerFicheros(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ficheroCargaCandidatosService.obtenerListadoFicheros());
		response.setMensaje("Listado de ficheros");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/cargaCandidatos/subirFichero")
	public ResponseEntity<ApiResponse> subirPlantilla(@RequestPart MultipartFile file 
			) {

		ApiResponse response = new ApiResponse();

		

		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ficheroCargaCandidatosService.subirFichero(file));
		response.setMensaje("Platilla subida.");

		return ResponseEntity.ok(response);

	}
	
	@DeleteMapping("/cargaCandidatos/borrar/{id}")
	public ResponseEntity<ApiResponse> borrarFichero(@PathVariable long id){
		
	ApiResponse response = new ApiResponse();

		

		response.setEstado(HttpStatus.OK);
		ficheroCargaCandidatosService.borrarFichero(id);
		response.setMensaje("Fichero borrado.");

		return ResponseEntity.ok(response);
		
		
	}
	
	@GetMapping("/cargaCandidatos/descargar/{id}")
	public ResponseEntity<Resource>descargar(@PathVariable long id, HttpServletRequest request){
	ApiResponse response = new ApiResponse();

		

		response.setEstado(HttpStatus.OK);
		Resource resource = ficheroCargaCandidatosService.descargarFichero(id);
		HttpHeaders header = new HttpHeaders();
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			
			   header.setContentType(MediaType.valueOf(contentType));
			   header.setContentLength(resource.getFile().length());
			   header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
			   header.set("Nombre", "attachment; filename=\"" + resource.getFilename() + "\"");
			   
		} catch (IOException ex) {
			throw new FileStorageException("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
	
		return new ResponseEntity<>(resource,header,HttpStatus.OK);
				
		
	}
	
	@PostMapping("cargaCandidatos/procesar/{id}")
	public ResponseEntity<ApiResponse>procesar(@PathVariable long id){

		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ficheroCargaCandidatosService.procesaFichero(id));
		response.setMensaje("Fichero procesado");
		
		return ResponseEntity.ok(response);
	}
		
}
