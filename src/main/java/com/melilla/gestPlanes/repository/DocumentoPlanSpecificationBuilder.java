package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.melilla.gestPlanes.DTO.CiudadanoCriterioBusqueda;
import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.service.PlanService;

import lombok.ToString;

@ToString
public class DocumentoPlanSpecificationBuilder {
	@Autowired
	private final PlanService planService;
	private final List<DocumentoCriterioBusqueda> parametros;
	
	public DocumentoPlanSpecificationBuilder(List<DocumentoCriterioBusqueda> criterios,PlanService planService) {
		this.parametros= criterios;
		this.planService= planService;
	}
	
	public Specification<DocumentoPlan> build(){
		if (parametros.size() == 0) return null;
		
		Specification<DocumentoPlan> result =new DocumentoPlanSpecification(parametros.get(0),planService);
		
		for (int 	i = 1; i < parametros.size(); i++) {
			
			result= Specification.where(result).and(new DocumentoPlanSpecification(parametros.get(i),planService));
			
		}
		
		return result;
		
	}

}
