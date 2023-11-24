package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Ciudadano;
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

	@PostMapping("/crearCiudadano")
	public ResponseEntity<ApiResponse>crearCiudadano(@RequestBody Ciudadano ciudadano){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ciudadanoService.crearCiudadano(ciudadano));
		
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearTrabajador/{idPlan}")
	public ResponseEntity<ApiResponse>crearTrabajador(@RequestBody CreateTrabajadorDTO trabajador,@PathVariable Long idPlan){
		

		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(null);
		
		
		return ResponseEntity.ok(response);
		
		
	}
}
