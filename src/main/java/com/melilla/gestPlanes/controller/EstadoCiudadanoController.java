package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.EstadoCiudadanoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EstadoCiudadanoController {
	
	@Autowired
	private EstadoCiudadanoService estadoCiudadanoService;
	
	
	@GetMapping("/estadosCiudadano")
	ResponseEntity<ApiResponse>getEstadosCiudadano(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(estadoCiudadanoService.obtenerEstados());
		response.setMensaje("Listado de estados");
		
		return ResponseEntity.ok(response);
		
		
	}

}
