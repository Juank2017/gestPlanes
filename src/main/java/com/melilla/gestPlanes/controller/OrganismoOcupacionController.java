package com.melilla.gestPlanes.controller;

import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateOrganismoOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditOrganismoOcupacionDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.OrganismoOcupacionService;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@Log
public class OrganismoOcupacionController {
	
	@Autowired
	private  OrganismoOcupacionService organismoOcupacionService;
	
	@PostMapping("/editOrganismoOcupacion")
	public ResponseEntity<ApiResponse>	editOrganismoOcupacion(@RequestBody EditOrganismoOcupacionDTO orgOcu) {
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(organismoOcupacionService.editOrganismoOcupacion(orgOcu));
		response.setMensaje("Actualizado.");
		
		
		return ResponseEntity.ok(response) ;
	}
	
	@PostMapping("/createOrganismoOcupacion")
	public ResponseEntity<ApiResponse>	createOrganismoOcupacion(@RequestBody CreateOrganismoOcupacionDTO orgOcu) {
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(organismoOcupacionService.createOrganismoOcupacion(orgOcu));
		response.setMensaje("Creado.");
		
		
		return ResponseEntity.ok(response) ;
	}
	
	
	
	
	
	

}
