package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.DestinoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DestinoController {
	
	@Autowired
	private DestinoService destinoService;
	
	@GetMapping("/destinos/{idOrganismo}")
	ResponseEntity<ApiResponse>getDestinosOrganismo(@PathVariable Long idOrganismo){
		
		ApiResponse response = new ApiResponse();
		response.getPayload().addAll(destinoService.obtenerDestinosOrganismo(idOrganismo));
		
		
		return ResponseEntity.ok(response);
	
	
	}

}
