package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.crearPresentacionDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Presentacion;
import com.melilla.gestPlanes.service.PresentacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PresentacionController {

	@Autowired
	PresentacionService presentacionService;

	@GetMapping("/presentaciones")
	public ResponseEntity<ApiResponse> obtenerPresentaciones() {
		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(presentacionService.presentaciones());
		response.setMensaje("Lista de presentaciones");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/presentacion/{idPresentacion}")
	public ResponseEntity<ApiResponse> obtenerPresentacion(@PathVariable Long idPresentacion) {

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);
		response.getPayload().add(presentacionService.buscarPresentacion(idPresentacion));
		response.setMensaje("Presentacion");

		return ResponseEntity.ok(response);

	}

	@PostMapping("/crearPresentacion")
	public ResponseEntity<ApiResponse> crearPresentacion(@RequestBody crearPresentacionDTO presentacion) {
		ApiResponse response = new ApiResponse();

		Presentacion present = new Presentacion();
		
		present.setPresentacion(presentacion.getPresentacion().toUpperCase());
		present.setObservaciones(presentacion.getObservaciones().toUpperCase());
		present.setResponsable(presentacion.getResponsable().toUpperCase());
		present.setVacaciones(presentacion.getVacaciones());
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(presentacionService.crearPresentacion(present));
		response.setMensaje("Presentacion creada");

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("borrarPresentacion/{idPresentacion}")
	public ResponseEntity<ApiResponse> borraPresentacion(@PathVariable Long idPresentacion) {
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		presentacionService.borrarPresentacion(idPresentacion);
		response.setMensaje("Presentacion borrada");

		return ResponseEntity.ok(response);
	}
	
	

}
