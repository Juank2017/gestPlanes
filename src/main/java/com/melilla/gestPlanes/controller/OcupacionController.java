package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.OcupacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OcupacionController {
	
	@Autowired
	private OcupacionService ocupacionService;
	
	@GetMapping("/ocupaciones/{idCategoria}")
	ResponseEntity<ApiResponse>getOcupacionPorCategoria(@PathVariable Long idCategoria){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ocupacionService.obtenerOcupacionesPorCategoria(idCategoria));
		response.setMensaje("Listado de ocupaciones de la categoria con id: "+idCategoria);
		
		return ResponseEntity.ok(response);
		
		
		
	}

}
