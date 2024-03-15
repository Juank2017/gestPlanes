package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.DashBoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DashBoardController {

	@Autowired
	DashBoardService dashBoardService;
	
	@GetMapping("/dashBoard/ciudadanoPorEstado/{idPlan}")
	ResponseEntity<ApiResponse> trabajadoresPorEstadoGenero(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.getPayload().addAll(dashBoardService.ciudadanosPorEstado(idPlan));
		
		return ResponseEntity.ok(response);
	}
	
}
