package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.service.CategoriaService;


@Service
public class CategoriaServiceImpl implements CategoriaService {

	@Autowired
	CategoriaRepository categoriaRpository;
	
	@Override
	public List<Categoria> obtenerCategoriasGrupo(Long idGrupo, Long idPlan) {
		
		return categoriaRpository.findAllByGrupoGrupoAndIdPlanIdPlanOrderByCategoriaAsc(idGrupo,idPlan);
	}

}
