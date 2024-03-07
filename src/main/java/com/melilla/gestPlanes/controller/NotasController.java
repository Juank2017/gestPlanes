package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearNotaDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.NotaCiudadano;
import com.melilla.gestPlanes.service.NotasService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
		response.setMensaje("Nota eliminada");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/nota/{idNota}")
	ResponseEntity<ApiResponse>nota(@PathVariable Long idNota){
		ApiResponse response  = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		
		response.getPayload().add(notasService.nota(idNota));
		response.setMensaje("Nota");
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/editarNota")
	ResponseEntity<ApiResponse>editarNota(@RequestBody NotaCiudadano nota){
		ApiResponse response  = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		
		response.getPayload().add(notasService.editaNota(nota));
		response.setMensaje("Nota editada.");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearNota")
	ResponseEntity<ApiResponse>crearNota(@RequestBody CrearNotaDTO nota){
		ApiResponse response  = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		
		response.getPayload().add(notasService.crearNota(nota));
		response.setMensaje("Nota creada.");
		
		return ResponseEntity.ok(response);
	}
	

}
