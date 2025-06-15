package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearPlantillaContratoDTO;
import com.melilla.gestPlanes.DTO.EditarPlantillaContratoDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.PlantillaContratoConfigService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequiredArgsConstructor
@Log
public class PlantillaContratoController {

	@Autowired
	private PlantillaContratoConfigService plantillaService;
	
	@GetMapping("/plan/plantillasContrato")
	ResponseEntity<ApiResponse>getPlantillas(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(plantillaService.obtenerPlantillas());
		response.setMensaje("Plantillas de contrato del plan activo");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/plan/plantillaContrato/{idPlantilla}")
	ResponseEntity<ApiResponse>getPlantilla(@PathVariable long idPlantilla){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(plantillaService.obtenerPlantillas());
		response.setMensaje("Plantillas de contrato del plan activo");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/plan/crearPlantillaContrato")
	ResponseEntity<ApiResponse>crearPlantilla(@RequestBody CrearPlantillaContratoDTO plantilla){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(plantillaService.crearPlantilla(plantilla));
		response.setMensaje("Plantilla creada");
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/plan/editarPlantillaContrato")
	ResponseEntity<ApiResponse>editarPlantilla(@RequestBody EditarPlantillaContratoDTO plantilla){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(plantillaService.editarPlantilla(plantilla));
		response.setMensaje("Plantilla editada");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/plan/borrarPlantilla/{idPlantilla}")
	ResponseEntity<ApiResponse>borrarPlantilla(@PathVariable long idPlantilla){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		plantillaService.borrarPlantilla(idPlantilla);
		response.setMensaje("Plantilla con id: "+idPlantilla+" borrada");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/plan/activarPlantilla/{idPlantilla}")
	ResponseEntity<ApiResponse>activaPlantilla(@PathVariable long idPlantilla){
		ApiResponse response= new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(plantillaService.activarPlantilla(idPlantilla));
		response.setMensaje("Plantilla activada");
		
		return ResponseEntity.ok(response);
	}
}
