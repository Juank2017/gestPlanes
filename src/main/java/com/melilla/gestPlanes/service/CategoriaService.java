package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.DTO.CrearCategoriaDTO;
import com.melilla.gestPlanes.DTO.EditarCategoriaDTO;
import com.melilla.gestPlanes.model.Categoria;

public interface CategoriaService {

	
	List<Categoria>obtenerCategoriasGrupo(Long idGrupo,Long idPlan);
	
	List<Categoria>obtenerCategoriasPlan(Long idPlan);
	
	Categoria editarCategoria(EditarCategoriaDTO categoria);
	
	void borrarCategoria(Long idCategoria);
	
	Categoria save(Categoria categoria);
	
	Categoria crearCategoria(CrearCategoriaDTO categoria);
	
	
}
