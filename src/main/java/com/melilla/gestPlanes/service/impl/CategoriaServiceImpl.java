package com.melilla.gestPlanes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.melilla.gestPlanes.DTO.CrearCategoriaDTO;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaConOcupacionesException;
import com.melilla.gestPlanes.exceptions.exceptions.CategoriaNotFoundException;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.repository.CategoriaRepository;
import com.melilla.gestPlanes.repository.PlanRepository;
import com.melilla.gestPlanes.service.CategoriaService;
import com.melilla.gestPlanes.service.PlanService;


@Service
public class CategoriaServiceImpl implements CategoriaService {

 
	@Autowired
	CategoriaRepository categoriaRpository;
	
	@Autowired
	PlanService planService;


	@Override
	public List<Categoria> obtenerCategoriasGrupo(Long idGrupo, Long idPlan) {
		
		List<Categoria> categorias = categoriaRpository.findAllByGrupoAndIdPlanIdPlanOrderByCategoriaAsc(idGrupo,idPlan);
		
		return categorias;
	}

	@Override
	public List<Categoria> obtenerCategoriasPlan(Long idPlan) {
		
		return categoriaRpository.findAllByIdPlanIdPlan(idPlan);
	}

	@Override
	public Categoria save(Categoria categoria) {
		
		return categoriaRpository.save(categoria);
	}

	@Override
	public void borrarCategoria(Long idCategoria) {
		
		Categoria categoria = categoriaRpository.findById(idCategoria).orElseThrow(()->new CategoriaNotFoundException(idCategoria));
		
		if (!categoria.getOcupaciones().isEmpty()) {
			throw new CategoriaConOcupacionesException(idCategoria);
		}else {
			categoriaRpository.delete(categoria);
		}
		
	}

	@Override
	public Categoria editarCategoria(Categoria categoria) {
		
		Categoria categoriaBBDD = categoriaRpository.findById(categoria.getIdCategoria()).orElseThrow(()->new CategoriaNotFoundException(categoria.getIdCategoria()));
		
		categoriaBBDD.setCategoria(categoria.getCategoria());
		categoriaBBDD.setGrupo(categoria.getGrupo());
		categoriaBBDD.setGrupoProfesionalPersonalLaboral(categoria.getGrupoProfesionalPersonalLaboral());
		
		
		
		
		return categoriaRpository.save(categoriaBBDD);
	}

	@Override
	public Categoria crearCategoria(CrearCategoriaDTO categoria) {
		
		Categoria nuevaCategoria = new Categoria();
		
		nuevaCategoria.setCategoria(categoria.getCategoria());
		nuevaCategoria.setGrupoProfesionalPersonalLaboral(categoria.getGrupoProfesionalPersonalLaboral());
		nuevaCategoria.setGrupo(categoria.getGrupo());
		nuevaCategoria.setIdPlan(planService.getPlanActivo());
		
		
		return categoriaRpository.save(nuevaCategoria);
	}

}
