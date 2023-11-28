package com.melilla.gestPlanes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.service.CiudadanoService;

import lombok.extern.java.Log;

@RestController
@Log
public class CiudadanoController {
	
	@Autowired
	private CiudadanoService ciudadanoService;
	
	@GetMapping("/ciudadanos")
	public ResponseEntity<ApiResponse> getCiudadanos(
			@RequestParam Long idPlan,
			@RequestParam int pageNumber,
			@RequestParam int pageSize,
			//@RequestParam(required = false) String[] sort,
			@RequestBody Map<String,String> order) {
		
		
		ApiResponse response = new ApiResponse();
		log.info(order.toString());
		
		Sort sort1 = null;
		List<Order> orders = new ArrayList<>();
		
		for (Map.Entry<String, String> o : order.entrySet()) {
			String campo = o.getKey();
			String direccion = o.getValue();
			Order orden= null;
			
			if (direccion.equals("asc")){
			 orden = Order.asc(campo);
			}else {
			 orden= Order.desc(campo);
			}
			
			orders.add(orden);
		}
		sort1 = Sort.by(orders);
		
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(ciudadanoService.getTrabajadores(idPlan, pageNumber, pageSize, sort1).getContent());
		response.setMensaje("Lista de ciudadanos");
		
		return ResponseEntity.ok(response);
		
	}

	@PostMapping("/crearCiudadano")
	public ResponseEntity<ApiResponse>crearCiudadano(@RequestBody Ciudadano ciudadano){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ciudadanoService.crearCiudadano(ciudadano));
		
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/crearTrabajador/{idPlan}")
	public ResponseEntity<ApiResponse>crearTrabajador(@RequestBody CreateTrabajadorDTO trabajador,@PathVariable Long idPlan){
		
		log.info(trabajador.toString());
        
		ApiResponse response = new ApiResponse();
		
		if(ciudadanoService.existeTrabajador(trabajador.getDNI())) {
			response.setMensaje("El trabajador con DNI: "+trabajador.getDNI()+ " ya existe!");
		}else {
			response.getPayload().add(ciudadanoService.crearTrabajador(trabajador));
			response.setMensaje("Registrado correctamente");
		}
		response.setEstado(HttpStatus.OK);
				
		return ResponseEntity.ok(response);
		
		
	}
}
