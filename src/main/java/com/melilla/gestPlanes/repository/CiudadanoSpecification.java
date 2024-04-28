package com.melilla.gestPlanes.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.melilla.gestPlanes.DTO.CiudadanoCriterioBusqueda;
import com.melilla.gestPlanes.model.Categoria;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Contrato;
import com.melilla.gestPlanes.model.Destino;
import com.melilla.gestPlanes.model.Ocupacion;
import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.model.Plan;
import com.melilla.gestPlanes.service.PlanService;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.ToString;


@ToString
public class CiudadanoSpecification implements Specification<Ciudadano> {

	private CiudadanoCriterioBusqueda criteria;

	private PlanService planService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CiudadanoSpecification(CiudadanoCriterioBusqueda criteria, PlanService planService) {
		this.criteria = criteria;
		this.planService = planService;
	}

	@Override
	public Predicate toPredicate(Root<Ciudadano> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

		Join<Ciudadano, Contrato> contratoCiudadano = root.join("contrato",JoinType.LEFT);
		Join<Contrato, Ocupacion> ocupacionContrato = contratoCiudadano.join("ocupacion",JoinType.LEFT);
		Join<Contrato, Categoria> categoriaContrato = contratoCiudadano.join("categoria",JoinType.LEFT);
		Join<Contrato, Organismo> organismoContrato = contratoCiudadano.join("entidad",JoinType.LEFT);
		Join<Contrato, Destino> destinoContrato = contratoCiudadano.join("destino",JoinType.LEFT);

	

		Plan plan = planService.getPlanActivo();
		
		Predicate likePredicate = null;
		Predicate fechaPredicate = null;
		Predicate equalPredicate = null;
		Predicate planPredicate = builder.equal(root.get("idPlan"), plan);

		switch (criteria.getId()) {

		
		
		case "fechaRegistro": case "fechaNacimiento": case "fechaListadoSepe":{
			 //convertir el valor a fecha
			String valor = this.criteria.getValue();
			try {
				LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

				fechaPredicate = builder.equal(root.get(this.criteria.getId()), fecha);
			} catch (Exception e) {

				return null;
			}
		}
		break;
		
		case "contrato.fechaInicio": case "contrato.fechaFin": case "contrato.fechaExtincion":{
			
			// convertir el valor a fecha
			String valor = this.criteria.getValue();
			try {
				LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

				fechaPredicate = builder.equal(contratoCiudadano.get(this.criteria.getId().replace("contrato.", "")), fecha);
			} catch (Exception e) {

				return null;
			}
			break;
		}
		
		case "contrato.ocupacion.ocupacion": {

			likePredicate = builder.like(ocupacionContrato.get("ocupacion"), "%" + criteria.getValue() + "%");
			break;
		}
		case "contrato.categoria.categoria": {
			likePredicate = builder.like(categoriaContrato.get("categoria"), "%" + criteria.getValue() + "%");
			break;
		}
		case "contrato.entidad.nombreCortoOrganismo": {
			likePredicate = builder.like(organismoContrato.get("nombreCortoOrganismo"),
					"%" + criteria.getValue() + "%");
			break;
		}
		case "contrato.destino.destino": {
			likePredicate = builder.like(destinoContrato.get("destino"), "%" + criteria.getValue() + "%");
			break;
		}
		case "idCiudadano":{
			equalPredicate = builder.equal(root.get("idCiudadano"), criteria.getValue());
			break;
		}
		case "DNI":{
			likePredicate = builder.like(root.get("DNI"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "nombre":{
			likePredicate = builder.like(root.get("nombre"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "apellido1":{
			likePredicate = builder.like(root.get("apellido1"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "apellido2":{
			likePredicate = builder.like(root.get("apellido2"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "seguridadSocial":{
			likePredicate = builder.like(root.get("seguridadSocial"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "estado":{
			likePredicate = builder.like(root.get("estado"), "%" + criteria.getValue() + "%");
			break;	
		}
		case "nacionalidad":{
			likePredicate = builder.like(root.get("nacionalidad"), "%" + criteria.getValue() + "%");
			break;	
		}

		}

		List<Predicate> predicados = new ArrayList<>();
		predicados.add(planPredicate);
		if (likePredicate != null)
			predicados.add(likePredicate);
		if (fechaPredicate != null)
			predicados.add(fechaPredicate);
		if(equalPredicate != null) predicados.add(equalPredicate);
		return builder.and(predicados.toArray(new Predicate[predicados.size()]));


	}

}
