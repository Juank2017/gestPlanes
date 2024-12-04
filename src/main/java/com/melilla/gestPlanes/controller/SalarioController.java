package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearSalarioDTO;
import com.melilla.gestPlanes.DTO.CrearSalarioDetalleDTO;
import com.melilla.gestPlanes.DTO.UpdateSalarioDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Salario;
import com.melilla.gestPlanes.repository.SalarioRepository;
import com.melilla.gestPlanes.service.SalarioDetalleService;
import com.melilla.gestPlanes.service.SalarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SalarioController {
	
	@Autowired
	SalarioService salarioService;
	
	@Autowired
	SalarioDetalleService salarioDetalleService;
	
	@GetMapping("/salario/{idPlan}")
	public ResponseEntity<ApiResponse> obtenerSalarios(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioService.obtenerSalariosPlan(idPlan));
		response.setMensaje("Salarios");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/salario/actualizar")
	public ResponseEntity<ApiResponse> actualizarSalario(@RequestBody UpdateSalarioDTO salario){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioService.actualizaSalario(salario));
		response.setMensaje("Salarios");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/salario/crearSalario")
	public ResponseEntity<ApiResponse> crearSalario(@RequestBody CrearSalarioDTO salario){
		ApiResponse response= new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(salarioService.crearSalario(salario));
		response.setMensaje("Salario creado");
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/salario/crearSalarioDetalle")
	public ResponseEntity<ApiResponse> crearSalarioDetalle(@RequestBody CrearSalarioDetalleDTO salario){
		ApiResponse response= new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(salarioDetalleService.crearSalarioDetalle(salario));
		response.setMensaje("Salario creado");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/salario/obtenerDetalleSalario/{idSalario}")
	public ResponseEntity<ApiResponse> obtenerDetalle(@PathVariable long idSalario){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioDetalleService.obtenerDetalleSalario(idSalario));
		response.setMensaje("Detalles del salario "+idSalario);
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/salario/obtenerDetalleSalarioActivo/{idSalario}")
	public ResponseEntity<ApiResponse> obtenerDetalleActivo(@PathVariable long idSalario){
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(salarioDetalleService.obtenerDetalleSalario(idSalario));
		response.setMensaje("Detalles del salario "+idSalario);
		
		return ResponseEntity.ok(response);
		
	}

}
