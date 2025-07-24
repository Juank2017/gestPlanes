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

import com.melilla.gestPlanes.DTO.CrearOrganismoDTO;
import com.melilla.gestPlanes.DTO.EditarOrganismoDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.service.OrganismoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrganismoController {
	
	@Autowired
	private OrganismoService organismoService;
	
	@GetMapping("/organismo/{idPlan}")
	ResponseEntity<ApiResponse>getOrganismoPorPlan(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(organismoService.obtenerOrganismosPorPlan(idPlan));
		response.setMensaje("Listado de organismos del plan: "+idPlan);
		
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/crearOrganismo")
	ResponseEntity<ApiResponse>CrearOrganismo(@RequestBody CrearOrganismoDTO organismo){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(organismoService.crearOrganismo(organismo));
		response.setMensaje("Organismo creado");
		
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/editarOrganismo")
	ResponseEntity<ApiResponse>EditarOrganismo(@RequestBody EditarOrganismoDTO organismo){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(organismoService.editarOrganismo(organismo));
		response.setMensaje("Organismo editado");
		
		
		return ResponseEntity.ok(response);
		
	}
	
	
	@GetMapping("/copiarOrganismosPlan/{idPlan}")
	ResponseEntity<ApiResponse>CopiarOrganismosPlan(@PathVariable long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(organismoService.copiarDeOtroPlan(idPlan));
		response.setMensaje("Organismos copiados");
		
		
		return ResponseEntity.ok(response);
		
	}
	
	@DeleteMapping("/borrarOrganismo/{idPlan}")
	ResponseEntity<ApiResponse>BorrarOrganismosPlan(@PathVariable long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		organismoService.borrarOrganismo(idPlan);
		response.setMensaje("Organismo borrado");
		
		
		return ResponseEntity.ok(response);
		
	}

}
