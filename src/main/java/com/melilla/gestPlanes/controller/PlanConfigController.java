package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.PlanConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@Log
public class PlanConfigController {
	
	@Autowired 
	PlanConfigService planConfigService;
	
	
	@GetMapping("/plan/{idPlan}/getConfig")
	ResponseEntity<ApiResponse>getConfig(@PathVariable long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planConfigService.obtenerConfig(idPlan));
		response.setMensaje("Obtenida configuración del plan");
		
		return ResponseEntity.ok(response);
		
	}
	
	
	

}
