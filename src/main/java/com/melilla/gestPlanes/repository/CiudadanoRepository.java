package com.melilla.gestPlanes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Ciudadano;




public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long> ,RevisionRepository<Ciudadano, Long, Long>, JpaSpecificationExecutor<Ciudadano> {
	
	Optional<Ciudadano> findByDNI(String DNI);
	Optional<Ciudadano>findByDNIAndEstado(String DNI, String estado);
	List<Ciudadano>findAllByDNIAndEstado(String DNI, String estado);
	boolean existsByDNI(String DNI);
	 Page<Ciudadano> findAll(Specification<Ciudadano> spec,Pageable page);
	 
	 List<Ciudadano> findByEstadoAndContratoEntidadIdOrganismoAndContratoOcupacionIdOcupacion(String estado,long idOrganismo,long idOcupacion);

	 List<Ciudadano> findByContratoEntidadIdOrganismoAndContratoOcupacionIdOcupacionAndEstadoIn(Long idOrganismo,Long idOcupacion,List<String>estados);
	 
	 Ciudadano findByEstadoAndDNI(String estado, String DNI);
	 
	 List<Ciudadano>findAllByPeriodosVacacionesIsNotNullAndIdPlanIdPlan(Long idPlan);
	 
	 @Query("SELECT c.estado ,sum(case when c.sexo = \"HOMBRE\" then 1 else 0 end) ,sum(case when c.sexo = \"MUJER\" then 1 else 0 end) FROM Ciudadano c WHERE c.idPlan.idPlan = ?1 GROUP BY c.estado")
	 List<Object>findAllByIdPlanIdPlanGroupByEstado(Long idPlan);
}

