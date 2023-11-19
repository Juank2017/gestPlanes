package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.service.CategoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoriaController {

	@Autowired
	CategoriaService categoriaService;
	
	@GetMapping("/categorias/{idPlan}/{idGrupo}")
	public ResponseEntity<ApiResponse>obtenerCategorias(@PathVariable Long idGrupo, @PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(categoriaService.obtenerCategoriasGrupo(idGrupo,idPlan));
		response.setMensaje("Lista de categorias del grupo "+idGrupo);
		
		
		return ResponseEntity.ok(response);
		
		
		
	}
}
