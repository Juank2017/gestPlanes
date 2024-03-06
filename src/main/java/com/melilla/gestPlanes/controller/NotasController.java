package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.NotasService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequiredArgsConstructor
public class NotasController {
	
	@Autowired
	NotasService notasService;
	
	@GetMapping("/notas/{idCiudadano}")
	public ResponseEntity<ApiResponse> notasTrabajador(@PathVariable Long idCiudadano) {
		
		ApiResponse response  = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(notasService.notasTrabajador(idCiudadano));
		response.setMensaje("Notas trabajador");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/borraNota/{idCiudadano}/{idNota}")
	public ResponseEntity<ApiResponse> borraNotaTrabajador(@PathVariable Long idCiudadano, @PathVariable Long idNota) {
		
		ApiResponse response  = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		notasService.borraNota(idNota);
		response.getPayload().addAll(notasService.notasTrabajador(idCiudadano));
		response.setMensaje("Notas trabajador");
		
		return ResponseEntity.ok(response);
	}
	
	

}
