package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.service.PlanService;

import lombok.ToString;

@ToString
public class DocumentoSpecificationBuilder {
	@Autowired
	private final PlanService planService;
	private final List<DocumentoCriterioBusqueda> parametros;
	
	public DocumentoSpecificationBuilder(List<DocumentoCriterioBusqueda> criterios,PlanService planService) {
		this.parametros= criterios;
		this.planService= planService;
	}
	
	public Specification<Documento> build(){
		if (parametros.size() == 0) return null;
		
		Specification<Documento> result =new DocumentoSpecification(parametros.get(0),planService);
		
		for (int 	i = 1; i < parametros.size(); i++) {
			
			result= Specification.where(result).and(new DocumentoSpecification(parametros.get(i),planService));
			
		}
		
		return result;
		
	}

}
