package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.CiudadanoService;

@RestController
public class CiudadanoController {
	
	@Autowired
	private CiudadanoService ciudadanoService;
	
	@GetMapping("/ciudadanos")
	public ResponseEntity<ApiResponse> getCiudadanos() {
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ciudadanoService.getCiudadanos());
		response.setMensaje("Lista de ciudadanos");
		
		return ResponseEntity.ok(response);
		
	}

}
