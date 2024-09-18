package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProcedimientoController {
	
	@Autowired
	ProcedimientoService procedimientoService;
	
	@GetMapping("/procedimientos")
	public ResponseEntity<ApiResponse>procedimientos(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(procedimientoService.obtenerProcedimientos());
		response.setMensaje("Listado de procedimientos");
		
		return ResponseEntity.ok(response);
	}

}
