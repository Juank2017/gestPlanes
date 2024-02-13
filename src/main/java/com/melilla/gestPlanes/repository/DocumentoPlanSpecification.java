package com.melilla.gestPlanes.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.melilla.gestPlanes.DTO.DocumentoCriterioBusqueda;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Documento;
import com.melilla.gestPlanes.model.DocumentoPlan;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.service.PlanService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class DocumentoPlanSpecification implements Specification<DocumentoPlan> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DocumentoCriterioBusqueda criterios;

	private PlanService planService;

	public DocumentoPlanSpecification(DocumentoCriterioBusqueda criterios, PlanService plan) {
		this.criterios = criterios;
		this.planService = plan;
	}

	@Override
	public Predicate toPredicate(Root<DocumentoPlan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		

		Plan plan = planService.getPlanActivo();

		String valor = null;
		Predicate likePredicate = null;

		Predicate fechaPredicate = null;
		Predicate planPredicate = builder.equal(root.get("idPlan"), plan);
		List<Predicate> listaPredicados = new ArrayList<Predicate>();
		listaPredicados.add(planPredicate);

		if (this.criterios.getId().equals("fechaDesde")) {
			valor = this.criterios.getValues();
			try {
				LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("yyy-MM-dd"));

				fechaPredicate = builder.greaterThan(root.get("createdAt"), fecha);
				listaPredicados.add(fechaPredicate);
			} catch (Exception e) {

				return null;
			}
		}

		if (this.criterios.getId().equals("fechaHasta")) {
			valor = this.criterios.getValues();
			try {
				LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				fechaPredicate = builder.lessThan(root.get("createdAt"), fecha);
				listaPredicados.add(fechaPredicate);
			} catch (Exception e) {

				return null;
			}
		}

		
		if (this.criterios.getId().equals("tipo")) {
			likePredicate = builder.like(root.<String>get(criterios.getId()), "%" + criterios.getValues() + "%");

			listaPredicados.add(likePredicate);
		}

		return builder.and(listaPredicados.toArray(new Predicate[listaPredicados.size()]));
	}

}
