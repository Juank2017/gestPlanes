package com.melilla.gestPlanes.repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.ToString;
import lombok.extern.java.Log;

@Log
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

		Join<Ciudadano, Contrato> contratoCiudadano = root.join("contrato");
		Join<Contrato, Ocupacion> ocupacionContrato = contratoCiudadano.join("ocupacion");
		Join<Contrato, Categoria> categoriaContrato = contratoCiudadano.join("categoria");
		Join<Contrato, Organismo> organismoContrato = contratoCiudadano.join("entidad");
		Join<Contrato, Destino> destinoContrato = contratoCiudadano.join("destino");

		Path<Object> mio = contratoCiudadano.get("ocupacion");

		log.warning("path " + mio.get("ocupacion"));

		Plan plan = planService.getPlanActivo();
		Long idPlan = plan.getIdPlan();
		Predicate likePredicate = null;
		Predicate fechaPredicate = null;
		Predicate planPredicate = builder.equal(root.get("idPlan"), plan);

		if (this.criteria.getId().equals("fechaRegistro") || this.criteria.getId().equals("fechaInicio")
				|| this.criteria.getId().equals("fechaFin") || this.criteria.getId().equals("fechaNacimiento") ||this.criteria.getId().equals("fechaExtincion")) {

			// convertir el valor a fecha
			String valor = this.criteria.getValue();
			try {
				LocalDate fecha = LocalDate.parse(valor, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

				fechaPredicate = builder.equal(root.get(this.criteria.getId()), fecha);
			} catch (Exception e) {

				return null;
			}

		}

		switch (criteria.getId()) {
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
		default:
			likePredicate = builder.like(root.<String>get(criteria.getId()), "%" + criteria.getValue() + "%");
		}

		List<Predicate> predicados = new ArrayList<>();
		predicados.add(planPredicate);
		if (likePredicate != null)
			predicados.add(likePredicate);
		if (fechaPredicate != null)
			predicados.add(fechaPredicate);
		return builder.and(predicados.toArray(new Predicate[predicados.size()]));
//		 if (root.get(criteria.getId()).getJavaType() == String.class) {
//			 
//             return builder.and(predicados.toArray(new Predicate[predicados.size()]));
//         } else {
//             return builder.equal(root.get(criteria.getId()),criteria.getValue());
//         }

	}

}
