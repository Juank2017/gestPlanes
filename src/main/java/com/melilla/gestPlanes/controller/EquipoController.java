package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.EquipoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EquipoController {
	
	@Autowired
	private EquipoService equipoService;
	
	@GetMapping("/equipos/{idPlan}")
	ResponseEntity<ApiResponse>equipos(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(equipoService.equipos(idPlan));
		response.setMensaje("Lista de equipos");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/equipo/{idPlan}/{idEquipo}")
	ResponseEntity<ApiResponse>equipo(@PathVariable Long idPlan, @PathVariable Long idEquipo){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(equipoService.equipo(idPlan,idEquipo));
		response.setMensaje("Equipo "+ idEquipo);
		
		return ResponseEntity.ok(response);
	}
}
