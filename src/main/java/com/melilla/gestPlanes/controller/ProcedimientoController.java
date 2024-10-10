package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateProcedimientoDTO;
import com.melilla.gestPlanes.DTO.UpdatePeriodosDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProcedimientoController {
	
	@Autowired
	ProcedimientoService procedimientoService;
	
	@GetMapping("/procedimientos")
	public ResponseEntity<ApiResponse>procedimientos(){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(procedimientoService.obtenerProcedimientos());
		response.setMensaje("Listado de procedimientos");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearProcedimiento")
	public ResponseEntity<ApiResponse>crearProcedimiento(@RequestBody CreateProcedimientoDTO procedimiento){

		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(procedimientoService.crearProcedimiento(procedimiento));
		response.setMensaje("Procedimiento Creado");
		
		return ResponseEntity.ok(response);
		}
	
	@GetMapping("/obtenerProcedimiento/{idProcedimiento}")
	public ResponseEntity<ApiResponse>obtenerProcedimiento(@PathVariable long idProcedimiento){
		
	ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(procedimientoService.getProcedimiento(idProcedimiento));
		response.setMensaje("Procedimiento");
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/actualizarPeriodoProcedimiento")
	public ResponseEntity<ApiResponse>insertarPeriodo(@RequestBody UpdatePeriodosDTO periodo){
ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(procedimientoService.updateContratoReclamadoProcedimiento(periodo));
		response.setMensaje("Periodo acutalizado");
		
		return ResponseEntity.ok(response);
	}
}
