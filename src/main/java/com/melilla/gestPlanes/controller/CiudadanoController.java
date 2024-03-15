package com.melilla.gestPlanes.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CiudadanoCriterioBusqueda;
import com.melilla.gestPlanes.DTO.CiudadanoCriterioOrden;
import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.service.CiudadanoService;

import lombok.extern.java.Log;

@RestController
@Log
public class CiudadanoController {

	@Autowired
	private CiudadanoService ciudadanoService;

	@PostMapping("/ciudadanos")
	public Page<Ciudadano> getCiudadanos(

			@RequestBody CiudadanoOrdenBusqueda ordenBusqueda) {

		ApiResponse response = new ApiResponse();
		log.info(ordenBusqueda.toString());

		Sort sort1 = null;
		List<Order> orders = new ArrayList<>();

//		for (Map.Entry<String, String> o : order.entrySet()) {
//			String campo = o.getKey();
//			String direccion = o.getValue();
//			Order orden= null;
//			
//			if (direccion.equals("asc")){
//			 orden = Order.asc(campo);
//			}else {
//			 orden= Order.desc(campo);
//			}
//			
//			orders.add(orden);
//		}
		sort1 = Sort.by(orders);

		response.setEstado(HttpStatus.OK);
		Page<Ciudadano> respuesta = ciudadanoService.getTrabajadores(ordenBusqueda);

		response.getPayload().add(respuesta);

		response.setMensaje("Lista de ciudadanos");

		return respuesta;

	}
//	@PostMapping("/ciudadanos")
//	public ResponseEntity<ApiResponse> getCiudadanos(
//
//			@RequestBody CiudadanoOrdenBusqueda ordenBusqueda) {
//
//		ApiResponse response = new ApiResponse();
//		log.info(ordenBusqueda.toString());
//
//		Sort sort1 = null;
//		List<Order> orders = new ArrayList<>();
//
////		for (Map.Entry<String, String> o : order.entrySet()) {
////			String campo = o.getKey();
////			String direccion = o.getValue();
////			Order orden= null;
////			
////			if (direccion.equals("asc")){
////			 orden = Order.asc(campo);
////			}else {
////			 orden= Order.desc(campo);
////			}
////			
////			orders.add(orden);
////		}
//		sort1 = Sort.by(orders);
//
//		response.setEstado(HttpStatus.OK);
//		Page<Ciudadano> respuesta = ciudadanoService.getTrabajadores(ordenBusqueda);
//
//		response.getPayload().add(respuesta);
//
//		response.setMensaje("Lista de ciudadanos");
//
//		return ResponseEntity.ok(response);
//
//	}

	@GetMapping("/ciudadano/{idCiudadano}")
	public ResponseEntity<ApiResponse> obtenerCiudadano(@PathVariable Long idCiudadano) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.getCiudadano(idCiudadano));

		return ResponseEntity.ok(response);
	}

	@PostMapping("/crearCiudadano")
	public ResponseEntity<ApiResponse> crearCiudadano(@RequestBody Ciudadano ciudadano) {

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);
		response.getPayload().add(ciudadanoService.crearCiudadano(ciudadano));

		return ResponseEntity.ok(response);
	}

	@PostMapping("/crearTrabajador/{idPlan}")
	public ResponseEntity<ApiResponse> crearTrabajador(@RequestBody CreateTrabajadorDTO trabajador,
			@PathVariable Long idPlan) {

		log.info(trabajador.toString());

		ApiResponse response = new ApiResponse();

//		if (ciudadanoService.existeTrabajador(trabajador.getDNI())) {
//			response.setMensaje("El trabajador con DNI: " + trabajador.getDNI() + " ya existe!");
//		} else {
//			response.getPayload().add(ciudadanoService.crearTrabajador(trabajador));
//			response.setMensaje("Registrado correctamente");
//		}
		response.getPayload().add(ciudadanoService.crearTrabajador(trabajador));
		response.setMensaje("Registrado correctamente");
		response.setEstado(HttpStatus.OK);

		return ResponseEntity.ok(response);

	}

	@PutMapping("/actualizaTrabajador")
	public ResponseEntity<ApiResponse> actualizaTrabajador(@RequestBody UpdateTrabajadorDTO2 trabajador) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.editaTrabajador(trabajador));
		response.setMensaje("Trabajador actualizado");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/modificaEstado")
	public ResponseEntity<ApiResponse> modificaEstadoTrabajador(@RequestBody List<ModificaEstadoDTO> trabajadores) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.modificarEstado(trabajadores));
		response.setMensaje("Estado del trabajador actualizado");

		return ResponseEntity.ok(response);
	}

	@GetMapping("/vacantes/{idOrganismo}/{idOcupacion}")
	public ResponseEntity<ApiResponse> trabajadoresContratadosPorOrganismoOcupacion(@PathVariable Long idOrganismo,
			@PathVariable Long idOcupacion) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.vacantesOrganismoOcupacion(idOrganismo, idOcupacion));
		response.setMensaje("Vacantes");

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/listadoVacantes")
	public ResponseEntity<ApiResponse>listadoVacantes(){
		ApiResponse response = new ApiResponse();

		response.getPayload().addAll(ciudadanoService.listadoVacantes());
		response.setMensaje("Listado de vacantes");

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/trabajadoresConVacaciones/{idPlan}")
	public ResponseEntity<ApiResponse>trabajadoresConVacaciones(@PathVariable Long idPlan){
		ApiResponse response = new ApiResponse();

		response.getPayload().addAll(ciudadanoService.trabajadoresConVacaciones(idPlan));
		response.setMensaje("Listado de vacantes");

		return ResponseEntity.ok(response);
	}

}
