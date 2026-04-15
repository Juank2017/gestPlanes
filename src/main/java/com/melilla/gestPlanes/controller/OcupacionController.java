package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearOcupacionDTO;
import com.melilla.gestPlanes.DTO.EditarOcupacionDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.OcupacionService;
import com.melilla.gestPlanes.service.PlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OcupacionController {
	
	@Autowired
	private OcupacionService ocupacionService;
	
	@Autowired
	private PlanService planService;
	
	@GetMapping("/ocupaciones/{idCategoria}")
	ResponseEntity<ApiResponse>getOcupacionPorCategoria(@PathVariable Long idCategoria){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ocupacionService.obtenerOcupacionesPorCategoria(idCategoria));
		response.setMensaje("Listado de ocupaciones de la categoria con id: "+idCategoria);
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@GetMapping("/ocupacionesPlan/{idPlan}")
	ResponseEntity<ApiResponse>getOcupacionPorPlan(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ocupacionService.obtenerOcupacionesPlan(planService.getPlan(idPlan)));
		response.setMensaje("Listado de ocupaciones de la categoria con id: "+idPlan);
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/borrarOcupacion/{idOcupacion}")
	ResponseEntity<ApiResponse>borrarOcupacion(@PathVariable Long idOcupacion){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		ocupacionService.borrarOcupacion(idOcupacion);
		response.setMensaje("Ocupación borrada");
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/crearOcupacion")
	ResponseEntity<ApiResponse>crearOcupacion(@RequestBody CrearOcupacionDTO ocupacion){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ocupacionService.crearOcupacion(ocupacion));
		response.setMensaje("Ocupación creada");
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/editarOcupacion")
	ResponseEntity<ApiResponse>editarOcupacion(@RequestBody EditarOcupacionDTO ocupacion){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ocupacionService.editarOcupacion(ocupacion));
		response.setMensaje("Ocupación editada");
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	
	

}
