package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.melilla.gestPlanes.DTO.CreatePlanDTO;
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
	public ResponseEntity<ApiResponse>crearPlan(@RequestBody CreatePlanDTO plan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.crearPlan(plan));
		response.setMensaje("Plan creado.");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/plan/activar/{idPlan}")
	public ResponseEntity<ApiResponse>activarPlan(@PathVariable long idPlan){
//		Long id = Long.parseLong(idPlan);
//		log.info(idPlan);
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.seleccionarPlan(idPlan));
		response.setMensaje("Plan activado");
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/planActivo")
	public ResponseEntity<ApiResponse>obtenerPlanActivo(){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.getPlanActivo());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/plan/copiarPlan")
	public ResponseEntity<ApiResponse>copiarPlan(@RequestParam String idPlan, @RequestParam String nombre){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.copiarPlan(Long.parseLong(idPlan), nombre));
		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/plan/copiarImportando")
	public ResponseEntity<ApiResponse>copiarImportando(@RequestParam String idPlan,@RequestParam String nombrePlan,@RequestParam MultipartFile file){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.copiarPlanImportando(Long.parseLong(idPlan), nombrePlan,file));
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/plan/actualizarPlan")
	public ResponseEntity<ApiResponse>actualizarPlan(@RequestParam Long idPlan,@RequestParam String denominacion){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(planService.actualizarPlan(idPlan,denominacion));
		response.setMensaje("Plan actualizado");
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/plan/borrarPlan/{idPlan}")
	public ResponseEntity<ApiResponse>borrarrPlan(@PathVariable Long idPlan){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		planService.borrarPlan(idPlan);
		response.setMensaje("Plan eliminado");
		return ResponseEntity.ok(response);
	}
	

}
