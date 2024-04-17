package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.TipoDocumentoPlanService;
import com.melilla.gestPlanes.service.TipoDocumentoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TipoDocumentoController {
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;
	@Autowired
	private TipoDocumentoPlanService tipoDocumentoPlanService;

	
	
	@GetMapping("/existeTipoDoc/{tipoDocumento}")
	ResponseEntity<ApiResponse>existeTipoDocumento(@PathVariable String tipoDocumento){
			
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		boolean resultado =  tipoDocumentoService.existeTipoDocumento(tipoDocumento );
		response.getPayload().add(resultado);
		String mensaje = (resultado)?"El tipoDocumento "+tipoDocumento+" ya existe.":"El tipoDocumento no existe"; 
		response.setMensaje(mensaje);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearTipoDoc/{tipoDocumento}")
	ResponseEntity<ApiResponse>crearTipoDocumento(@PathVariable String tipoDocumento){

		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(tipoDocumentoService.crearTipoDocumento( tipoDocumento));
		response.setMensaje("Creado el tipoDocumento "+tipoDocumento);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/existeTipoDocPlan/{tipoDocumento}")
	ResponseEntity<ApiResponse>existeTipoDocumentoPlan(@PathVariable String tipoDocumento){
			
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		boolean resultado =  tipoDocumentoPlanService.existeTipoDocumento(tipoDocumento );
		response.getPayload().add(resultado);
		String mensaje = (resultado)?"El tipoDocumento "+tipoDocumento+" ya existe.":"El tipoDocumento no existe"; 
		response.setMensaje(mensaje);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearTipoDocPlan/{tipoDocumento}")
	ResponseEntity<ApiResponse>crearTipoDocumentoPlan(@PathVariable String tipoDocumento){

		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(tipoDocumentoPlanService.crearTipoDocumento( tipoDocumento));
		response.setMensaje("Creado el tipoDocumento "+tipoDocumento);
		
		return ResponseEntity.ok(response);
	}

}
