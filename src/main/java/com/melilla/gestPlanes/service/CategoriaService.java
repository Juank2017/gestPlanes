package com.melilla.gestPlanes.service;

import java.util.List;

import com.melilla.gestPlanes.model.Categoria;

public interface CategoriaService {

	
	List<Categoria>obtenerCategoriasGrupo(Long idGrupo,Long idPlan);
}
