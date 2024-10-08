package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateNominaDTO;
import com.melilla.gestPlanes.DTO.EditaNominaReclamadaDTO;
import com.melilla.gestPlanes.mappers.nominasReclamadasMapper;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.NominasReclamadas;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.service.ContratoReclamadoService;
import com.melilla.gestPlanes.service.NominaReclamadaService;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor
public class ContratoReclamadoController {

	@Autowired
	ContratoReclamadoService contratoReclamadoService;

	@Autowired
	ProcedimientoService procedimientoService;

	@Autowired
	NominaReclamadaService nominasService;

	@Autowired
	private final nominasReclamadasMapper nominasMapper;

	@PostMapping("/insertarNominaContratoReclamado")
	ResponseEntity<ApiResponse> insertarNomina(@RequestBody CreateNominaDTO nomina) {

		ContratoReclamado contrato = contratoReclamadoService.getContrato(nomina.getIdContratoReclamado());

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);

		contratoReclamadoService.insertarNominaEnContrato(contrato, nomina);
		response.getPayload().add(procedimientoService.getProcedimiento(nomina.getIdProcedimiento()));

		response.getPayload().add(nomina.getIdContratoReclamado());

		response.setMensaje("Nómina insertada");

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/eliminarNominaContratoReclamado/{idNomina}/{idProcedimiento}")
	ResponseEntity<ApiResponse> eliminarNomina(@PathVariable long idNomina, @PathVariable long idProcedimiento) {

		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);

		nominasService.eliminaNomina(idNomina);

		response.getPayload().add(procedimientoService.getProcedimiento(idProcedimiento));

		response.setMensaje("Nómina eliminada");

		return ResponseEntity.ok(response);

	}

	@PutMapping("/editarNominaContratoReclamado")
	ResponseEntity<ApiResponse> editaNomina(@RequestBody EditaNominaReclamadaDTO nomina) {
		ApiResponse response = new ApiResponse();

		response.setEstado(HttpStatus.OK);

		NominasReclamadas nominaEditada = nominasService.getNomina(nomina.getIdNomina());

		nominaEditada = nominasMapper.updateNominasReclamadasFromEditaNominaReclamadaDTO(nomina, nominaEditada);
		
		nominasService.editarNomina(nominaEditada);

		response.getPayload().add(procedimientoService.getProcedimiento(nomina.getIdProcedimiento()));

		response.setMensaje("Nómina actualizada.");

		return ResponseEntity.ok(response);
	}

}
