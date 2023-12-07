package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.melilla.gestPlanes.DTO.CiudadanoCriterioBusqueda;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.service.PlanService;

import lombok.ToString;

@ToString
public class CiudadanoSpecificationBuilder {
	@Autowired
	private final PlanService planService;
	private final List<CiudadanoCriterioBusqueda> parametros;
	
	public CiudadanoSpecificationBuilder(List<CiudadanoCriterioBusqueda> criterios,PlanService planService) {
		this.parametros= criterios;
		this.planService= planService;
	}
	
	public Specification<Ciudadano> build(){
		if (parametros.size() == 0) return null;
		
		Specification<Ciudadano> result =new CiudadanoSpecification(parametros.get(0),planService);
		
		for (int 	i = 1; i < parametros.size(); i++) {
			
			result= Specification.where(result).and(new CiudadanoSpecification(parametros.get(i),planService));
			
		}
		
		return result;
		
	}

}
