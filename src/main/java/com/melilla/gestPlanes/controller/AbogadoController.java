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

import com.melilla.gestPlanes.DTO.CrearAbogadoDto;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.AbogadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AbogadoController {
	
	@Autowired
	private AbogadoService abogadoService;
	
	
	@GetMapping("/abogados")
	ResponseEntity<ApiResponse> listadoAbogados(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(abogadoService.abogados());
		response.setMensaje("Listado de Abogados");
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/crearAbogado")
	ResponseEntity<ApiResponse> crearAbogado(@RequestBody CrearAbogadoDto abogado){
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(abogadoService.crearAbogado(abogado));
		response.setMensaje("Abogado creado");
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/borrarAbogado/{idAbogado}")
	ResponseEntity<ApiResponse> borrarAbogado(@PathVariable Long idAbogado){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		abogadoService.borrarAbogado(idAbogado);
		response.setMensaje("Abogado eliminado");
		return ResponseEntity.ok(response);
		
		
	}

}
