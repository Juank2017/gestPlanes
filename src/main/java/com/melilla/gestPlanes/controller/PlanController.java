package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@Log
public class PlanController {
	
	@Autowired
	private PlanService planService;
	
	@GetMapping("/planes")
	public ResponseEntity<ApiResponse>getPlanes(){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(planService.getPlanes());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/plan/{idPlan}")
	public ResponseEntity<ApiResponse>getPlan(@PathVariable String idPlan){
		
		Long id = Long.parseLong(idPlan);
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.getPlan(id));
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/plan/crearPlan")
	public ResponseEntity<ApiResponse>crearPlan(@RequestBody Plan plan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.crearPlan(plan));
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/plan/activar")
	public ResponseEntity<ApiResponse>activarPlan(@RequestParam String idPlan){
		Long id = Long.parseLong(idPlan);
		log.info(idPlan);
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.seleccionarPlan(id));
		return ResponseEntity.ok(response);
	}
	

}
