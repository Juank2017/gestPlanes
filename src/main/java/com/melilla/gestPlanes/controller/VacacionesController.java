package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.VacacionesService;
import com.sun.mail.iap.Response;

@RestController
public class VacacionesController {
	
	@Autowired
	VacacionesService vacacionesService;
	
	@GetMapping("/vacaciones/{idPlan}")
	ResponseEntity<ApiResponse>vacaciones(@PathVariable Long idPlan){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(vacacionesService.listadoVacaciones(idPlan));
		response.setMensaje("Listado Vacaciones");
		
		return ResponseEntity.ok(response);
	}

}
