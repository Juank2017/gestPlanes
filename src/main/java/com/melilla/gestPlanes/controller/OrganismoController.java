package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.OrganismoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrganismoController {
	
	@Autowired
	private OrganismoService organismoService;
	
	@GetMapping("/organismo/{idPlan}")
	ResponseEntity<ApiResponse>getOrganismoPorPlan(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(organismoService.obtenerOrganismosPorPlan(idPlan));
		response.setMensaje("Listado de organismos del plan: "+idPlan);
		
		
		return ResponseEntity.ok(response);
		
	}

}
