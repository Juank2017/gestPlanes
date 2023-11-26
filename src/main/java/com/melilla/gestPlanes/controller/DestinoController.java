package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.DestinoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DestinoController {
	
	@Autowired
	private DestinoService destinoService;
	
	@GetMapping("/destinos/{idOrganismo}")
	ResponseEntity<ApiResponse>getDestinosOrganismo(@PathVariable Long idOrganismo){
		
		ApiResponse response = new ApiResponse();
		response.getPayload().addAll(destinoService.obtenerDestinosOrganismo(idOrganismo));
		
		
		return ResponseEntity.ok(response);
	
	
	}
	
	@GetMapping("/existeDestino/{idOrganismo}/{destino}")
	ResponseEntity<ApiResponse>existeDestino(@PathVariable Long idOrganismo,@PathVariable String destino){
			
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		boolean resultado =  destinoService.existeDestino(destino , idOrganismo);
		response.getPayload().add(resultado);
		String mensaje = (resultado)?"El destino "+destino+" ya existe.":"El destino no existe"; 
		response.setMensaje(mensaje);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearDestino/{idOrganismo}/{destino}")
	ResponseEntity<ApiResponse>crearDestino(@PathVariable Long idOrganismo,@PathVariable String destino){

		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(destinoService.crearDestino(idOrganismo, destino));
		response.setMensaje("Creado el destino "+destino);
		
		return ResponseEntity.ok(response);
	}

}
