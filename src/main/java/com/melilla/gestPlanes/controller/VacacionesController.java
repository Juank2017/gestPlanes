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

import com.melilla.gestPlanes.DTO.AltaVacacionesDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.VacacionesService;
import com.sun.mail.iap.Response;

@RestController
public class VacacionesController {
	
	@Autowired
	VacacionesService vacacionesService;
	
	@GetMapping("/vacaciones/{idPlan}")
	ResponseEntity<ApiResponse>vacaciones(@PathVariable Long idPlan){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(vacacionesService.listadoVacaciones(idPlan));
		response.setMensaje("Listado Vacaciones");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/vacacionesTrabajador/{idTrabajador}")
	ResponseEntity<ApiResponse>vacacionesTrabajador(@PathVariable Long idTrabajador){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(vacacionesService.vacacionesTrabajador(idTrabajador));
		response.setMensaje("Vacaciones trabajador");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/altaPeriodoVacaciones")
	ResponseEntity<ApiResponse>altaPeriodo(@RequestBody AltaVacacionesDTO periodo){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(vacacionesService.altaPeriodo(periodo));
		response.getPayload().addAll(vacacionesService.vacacionesTrabajador(periodo.getIdCiudadano()));
		response.setMensaje("Periodo registrado.");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/bajaPeriodo/{idPeriodo}")
	ResponseEntity<ApiResponse>bajaPeriodo(@PathVariable Long idPeriodo){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		vacacionesService.bajaPeriodo(idPeriodo);
		
		response.setMensaje("Periodo eliminado.");
		
		return ResponseEntity.ok(response);
	}

}
