//package com.melilla.gestPlanes.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.melilla.gestPlanes.exceptions.exceptions.ExpedienteNotFoundException;
//import com.melilla.gestPlanes.model.ApiResponse;
//import com.melilla.gestPlanes.service.ExpedienteService;
//
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequiredArgsConstructor
//public class ExpedienteController {
//	
//	@Autowired
//	private ExpedienteService expedienteService;
//	
//	@GetMapping("/expedientes")
//	ResponseEntity<ApiResponse>getExpedientes(){
//		ApiResponse response = new ApiResponse();
//		response.setEstado(HttpStatus.OK);
//		response.getPayload().addAll(expedienteService.getExpedientes());
//		response.setMensaje("Listado de expedientes");
//		
//		return ResponseEntity.ok(response);
//	}
//	
//	@GetMapping("/expediente/{DNI}")
//	ResponseEntity<ApiResponse>getExpedienteByDNI(@PathVariable String DNI){
//		ApiResponse response = new ApiResponse();
//		response.setEstado(HttpStatus.OK);
//		response.getPayload().add(expedienteService.getExpedientaByCiudadanoDNI(DNI).orElseThrow(()->new ExpedienteNotFoundException(1l)));
//	
//		return ResponseEntity.ok(response);
//	}
//	
//	@GetMapping("/expedientes/{ocupacion}")
//	ResponseEntity<ApiResponse>getExpedienteByOcupacion(@PathVariable String ocupacion){
//		ApiResponse response = new ApiResponse();
//		response.setEstado(HttpStatus.OK);
//		response.getPayload().add(expedienteService.getExpedienteByOcupacion(ocupacion));
//	
//		return ResponseEntity.ok(response);
//	}
//	
//	
//
//}
