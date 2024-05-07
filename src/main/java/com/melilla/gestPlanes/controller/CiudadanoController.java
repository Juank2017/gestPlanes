package com.melilla.gestPlanes.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CiudadanoOrdenBusqueda;
import com.melilla.gestPlanes.DTO.CreateTrabajadorDTO;
import com.melilla.gestPlanes.DTO.ModificaEstadoDTO;
import com.melilla.gestPlanes.DTO.ModificaFechaContratoDTO;
import com.melilla.gestPlanes.DTO.ModificarOrganismoContrato;
import com.melilla.gestPlanes.DTO.UpdateTrabajadorDTO2;
import com.melilla.gestPlanes.DTO.listadoTrabajadoresDTO;
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
	public List<listadoTrabajadoresDTO> getCiudadanos(

			@RequestBody CiudadanoOrdenBusqueda ordenBusqueda) {

		ApiResponse response = new ApiResponse();
		log.info(ordenBusqueda.toString());
		List<listadoTrabajadoresDTO> listado = new ArrayList<listadoTrabajadoresDTO>();
		Sort sort1 = null;
		List<Order> orders = new ArrayList<>();


		sort1 = Sort.by(orders);

		response.setEstado(HttpStatus.OK);
		Page<Ciudadano> respuesta = ciudadanoService.getTrabajadores(ordenBusqueda);
		
		for (Ciudadano ciudadano : respuesta) {
			listadoTrabajadoresDTO item = new listadoTrabajadoresDTO();
			item.setIdCiudadano(ciudadano.getIdCiudadano());
			item.setNumeroOrdenSepe(ciudadano.getNumeroOrdenSepe());
			item.setSinClausula(ciudadano.isSinClausula());
			item.setBajaLaboral(ciudadano.isBajaLaboral());
			item.setBajaMaternal(ciudadano.isBajaMaternal());
			item.setEstado(ciudadano.getEstado());
			item.setFechaRegistro(ciudadano.getFechaRegistro());
			item.setFechaListadoSepe(ciudadano.getFechaListadoSepe());
			item.setCcc(ciudadano.getCcc());
			item.setNacionalidad(ciudadano.getNacionalidad());
			if(ciudadano.getContrato() != null) {
				item.setFechaInicio(ciudadano.getContrato().getFechaInicio());
				item.setFechaFinal(ciudadano.getContrato().getFechaFinal());
				item.setFechaExtincion(ciudadano.getContrato().getFechaExtincion());
				item.setTotal((ciudadano.getContrato().getTotal()!=null)?ciudadano.getContrato().getTotal():null);
				item.setCategoria((ciudadano.getContrato().getCategoria()!= null)?ciudadano.getContrato().getCategoria().getCategoria(): null);
				item.setOcupacion((ciudadano.getContrato().getOcupacion()!= null)?ciudadano.getContrato().getOcupacion().getOcupacion():null);
				item.setOrganismo((ciudadano.getContrato().getEntidad()!= null)?ciudadano.getContrato().getEntidad().getOrganismo():null);
				item.setDestino((ciudadano.getContrato().getDestino()!=null)?ciudadano.getContrato().getDestino().getDestino():null);
			}
			item.setNombre(ciudadano.getNombre());
			item.setApellido1(ciudadano.getApellido1());
			item.setApellido2(ciudadano.getApellido2());
			item.setDNI(ciudadano.getDNI());
			item.setSeguridadSocial(ciudadano.getSeguridadSocial());
			item.setFechaNacimiento(ciudadano.getFechaNacimiento());
			item.setAntecedentes(ciudadano.isAntecedentes());
			item.setAltaSS(ciudadano.isAltaSS());
			item.setContrata(ciudadano.isContrata());
			item.setEscaneado(ciudadano.isEscaneado());
			item.setNedaes(ciudadano.isNedaes());
			item.setTelefono(ciudadano.getTelefono());
			item.setSexo(ciudadano.getSexo());
			item.setDeleted(ciudadano.isDeleted());
			item.setTotalElements(respuesta.getTotalElements());
			listado.add(item);
		}
		response.getPayload().add(respuesta);

		response.setMensaje("Lista de ciudadanos");

		return listado;

	}


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
	
	@PostMapping("/modificaFechaContrato")
	public ResponseEntity<ApiResponse> modificaFechaContratoTrabajador(@RequestBody List<ModificaFechaContratoDTO> trabajadores) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.modificarFechaContrato(trabajadores));
		response.setMensaje("Estado del trabajador actualizado");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/modificaOrganismoContrato")
	public ResponseEntity<ApiResponse> modificaOrganismoContratoTrabajador(@RequestBody List<ModificarOrganismoContrato> trabajadores) {
		ApiResponse response = new ApiResponse();

		response.getPayload().add(ciudadanoService.modificarOrganismoContrato(trabajadores));
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
	
	@DeleteMapping("/borraTrabajador/{idCiudadano}")
	public ResponseEntity<ApiResponse>deleteTrabajador(@PathVariable Long idCiudadano){
		ApiResponse response = new ApiResponse();

		ciudadanoService.deleteTrabajador(idCiudadano);
		response.setMensaje("Trabajador eliminado.");

		return ResponseEntity.ok(response);
	}
	
	@PutMapping("/restauraTrabajador/{idCiudadano}")
	public ResponseEntity<ApiResponse>restauraTrabajador(@PathVariable Long idCiudadano){
		ApiResponse response = new ApiResponse();

		ciudadanoService.restoreTrabajador(idCiudadano);
		response.setMensaje("Trabajador restaurado.");

		return ResponseEntity.ok(response);
	}

}
