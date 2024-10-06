package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CreateNominaDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.ContratoReclamado;
import com.melilla.gestPlanes.model.Procedimiento;
import com.melilla.gestPlanes.service.ContratoReclamadoService;
import com.melilla.gestPlanes.service.NominaReclamadaService;
import com.melilla.gestPlanes.service.ProcedimientoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContratoReclamadoController {
	
	@Autowired
	ContratoReclamadoService contratoReclamadoService;
	
	@Autowired
	ProcedimientoService procedimientoService;
	
	@Autowired
	NominaReclamadaService nominasService;
	
	@PostMapping("/insertarNominaContratoReclamado")
	ResponseEntity<ApiResponse>insertarNomina(@RequestBody CreateNominaDTO nomina){
		
		
		
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
	ResponseEntity<ApiResponse>eliminarNomina(@PathVariable long idNomina,@PathVariable long idProcedimiento){
		
ApiResponse response = new ApiResponse();
		
		response.setEstado(HttpStatus.OK);
		
		
		nominasService.eliminaNomina(idNomina);
		
		response.getPayload().add(procedimientoService.getProcedimiento(idProcedimiento));
		
		response.setMensaje("Nómina eliminada");
		
		return ResponseEntity.ok(response);
		
		
	}

}
