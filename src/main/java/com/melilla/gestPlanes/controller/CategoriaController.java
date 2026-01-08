package com.melilla.gestPlanes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.melilla.gestPlanes.DTO.CrearCategoriaDTO;
import com.melilla.gestPlanes.DTO.EditarCategoriaDTO;
import com.melilla.gestPlanes.model.ApiResponse;
import com.melilla.gestPlanes.model.Categoria;
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
	
	
	@GetMapping("/categorias/{idPlan}")
	public ResponseEntity<ApiResponse>obtenerCategoriasPlan( @PathVariable Long idPlan){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().addAll(categoriaService.obtenerCategoriasPlan(idPlan));
		response.setMensaje("Lista de categorias del plan");
		
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/borrarCategoria/{idCategoria}")
	public ResponseEntity<ApiResponse>borrarCategoria( @PathVariable Long idCategoria){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		categoriaService.borrarCategoria(idCategoria);
		response.setMensaje("Categoría borrada.");
		
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/editarCategoria")
	public ResponseEntity<ApiResponse>editarCategoria( @RequestBody EditarCategoriaDTO categoria){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(categoriaService.editarCategoria(categoria));
		response.setMensaje("Categoría editada.");
		
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
	@PostMapping("/crearCategoria")
	public ResponseEntity<ApiResponse>CrearCategoria( @RequestBody CrearCategoriaDTO categoria){
		
		ApiResponse response = new ApiResponse();
		response.setEstado(HttpStatus.OK);
		response.getPayload().add(categoriaService.crearCategoria(categoria));
		response.setMensaje("Categoría creada.");
		
		
		return ResponseEntity.ok(response);
		
		
		
	}
	
}
