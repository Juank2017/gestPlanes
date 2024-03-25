package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.service.SalarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SalarioController {
	
	@Autowired
	SalarioService salarioService;
	
	@GetMapping("/salario/{idPlan}")
	public ResponseEntity<ApiResponse> obtenerSalarios(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioService.obtenerSalarios(idPlan));
		response.setMensaje("Salarios");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/salario/actualizar")
	public ResponseEntity<ApiResponse> actualizarSalario(@RequestBody Salario salario){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioService.actualizaSalario(salario));
		response.setMensaje("Salarios");
		
		return ResponseEntity.ok(response);
	}

}
