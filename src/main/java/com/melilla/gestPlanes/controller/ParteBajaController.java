package com.melilla.gestPlanes.controller;

import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearParteBajaDTO;
import com.melilla.gestPlanes.DTO.CrearParteConfirmacionDTO;
import com.melilla.gestPlanes.DTO.EditaContingenciaDTO;
import com.melilla.gestPlanes.DTO.EditaParteBajaDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.ParteBajaService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class ParteBajaController {
	
	@Autowired
	private ParteBajaService parteBajaService;
	
	
	@GetMapping("/partesBaja/{idTrabajador}")
	public ResponseEntity<ApiResponse> obtenerPartesTrabajador(@PathVariable long idTrabajador) {
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(parteBajaService.obtenerPartesBajaTrabajadorMap(idTrabajador));
		response.setMensaje("Listado de partes de baja del trabajador con id: "+idTrabajador);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/partesBajaPorDNI/{DNI}")
	public ResponseEntity<ApiResponse> obtenerPartesTrabajador(@PathVariable String DNI) {
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(parteBajaService.obtenerPartesBajaTrabajadorPorDNI(DNI));
		response.setMensaje("Listado de partes de baja del trabajador con DNI: "+DNI);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/parteBaja/alta")
	public ResponseEntity<ApiResponse> altaParteBaja(@RequestBody CrearParteBajaDTO  parte){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(parteBajaService.altaParteBaja(parte));
		response.setMensaje("Creado parte de baja");
		
		return ResponseEntity.ok(response);
		
		
	}
	
	@PostMapping("/parteBaja/edita")
	public ResponseEntity<ApiResponse> editaParteBaja(@RequestBody EditaParteBajaDTO  parte){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(parteBajaService.editaParteBaja(parte));
		response.setMensaje("Parte de baja editado");
		
		return ResponseEntity.ok(response);
		
		
	}
	
	@PostMapping("/parteBaja/parteConfirmacion/alta")
	public ResponseEntity<ApiResponse> altaParteConfirmacion(@RequestBody CrearParteConfirmacionDTO  parte){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(parteBajaService.insertaParteConfirmacion(parte));
		response.setMensaje("Creado parte de confirmación");
		
		return ResponseEntity.ok(response);
		
		
	}
	
	@GetMapping("/parteBaja/tiposContingencia")
	public ResponseEntity<ApiResponse> listadoTiposContingencia(){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(parteBajaService.listaContingencias());
		response.setMensaje("Lista de tipos de contingencia.");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/parteBaja/CrearContingencia")
	public ResponseEntity<ApiResponse> creaContingencia(@RequestBody String contingencia){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(parteBajaService.crearContingencia(contingencia));
		response.setMensaje("Tipo de contingencia creado.");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/parteBaja/EditarContingencia")
	public ResponseEntity<ApiResponse> editaContingencia(@RequestBody EditaContingenciaDTO contingencia){
	ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(parteBajaService.editarContingencia(contingencia));
		response.setMensaje("Editado.");
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/parteBaja/borraContingencia/{idTipoContingencia}")
	public ResponseEntity<ApiResponse>borraContingencia(@PathVariable long idTipoContingencia){
ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		parteBajaService.borraContingencia(idTipoContingencia);
		response.setMensaje("Editado.");
		
		return ResponseEntity.ok(response);
	}

}
