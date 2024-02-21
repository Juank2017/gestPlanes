package com.melilla.gestPlanes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.exceptions.exceptions.CiudadanoNotFoundException;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Equipo;
import com.melilla.gestPlanes.service.CiudadanoService;
import com.melilla.gestPlanes.service.EquipoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class EquipoController {
	
	@Autowired
	private EquipoService equipoService;
	
	@Autowired
	private CiudadanoService ciudadanoService;
	
	@GetMapping("/equipos/{idPlan}")
	ResponseEntity<ApiResponse>equipos(@PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(equipoService.equipos(idPlan));
		response.setMensaje("Lista de equipos");
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/equipo/{idPlan}/{idEquipo}")
	ResponseEntity<ApiResponse>equipo(@PathVariable Long idPlan, @PathVariable Long idEquipo){
		
		ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(equipoService.equipo(idPlan,idEquipo));
		response.setMensaje("Equipo "+ idEquipo);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/removeComponent/{idPlan}/{idEquipo}/{idCiudadano}")
	ResponseEntity<ApiResponse>removeComponent(@PathVariable Long idPlan,@PathVariable Long idEquipo,@PathVariable Long idCiudadano){
		ApiResponse response = new ApiResponse();
		
		Ciudadano ciudadano = ciudadanoService.getCiudadano(idCiudadano);
		Equipo equipo = equipoService.equipo(idPlan, idEquipo);
		
		response.setEstado(HttpStatus.OK);
		equipoService.removeComponente(equipo,ciudadano );
		response.getPayload().add(equipoService.equipo(idPlan,idEquipo));
		response.setMensaje("Trabajador "+ ciudadano.getNombre()+" "+ciudadano.getApellido1()+" eliminado del equipo "+equipo.getNombreEquipo());
		
		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/addComponent/{idPlan}/{idEquipo}/{nuevoComponente}")
	ResponseEntity<ApiResponse>addComponent(@PathVariable Long idPlan,@PathVariable Long idEquipo,@PathVariable String nuevoComponente){
		ApiResponse response = new ApiResponse();
		
		List<Ciudadano> ciudadano = ciudadanoService.getAllTrabajadorByDNIAndEstado(nuevoComponente,"CONTRATADO/A");
		if(ciudadano.isEmpty()) {
			throw new CiudadanoNotFoundException(1l);
		}else{
			if (ciudadano.size() > 1) {
				response.setEstado(HttpStatus.BAD_REQUEST);
				response.getPayload().add(equipoService.equipo(idPlan,idEquipo));
				response.setMensaje("Hay varios registros del DNI "+nuevoComponente+" con el estado CONTRATADO/A");
			}else {
				Equipo equipo = equipoService.equipo(idPlan, idEquipo);
				response.setEstado(HttpStatus.OK);
				equipoService.addComponente(equipo,ciudadano.get(0) );
				response.getPayload().add(equipoService.equipo(idPlan,idEquipo));
				response.setMensaje("Trabajador "+ ciudadano.get(0).getNombre()+" "+ciudadano.get(0).getApellido1()+" añadido al equipo "+equipo.getNombreEquipo());
			}
		};
		
		
		
		
		return ResponseEntity.ok(response);
	}
}
