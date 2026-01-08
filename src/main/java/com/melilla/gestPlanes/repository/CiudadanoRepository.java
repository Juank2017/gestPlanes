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

import com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO;
import com.melilla.gestPlanes.model.Ciudadano;
import com.melilla.gestPlanes.model.Plan;

public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long>, RevisionRepository<Ciudadano, Long, Long>,
		JpaSpecificationExecutor<Ciudadano> {

	Optional<Ciudadano> findByDNI(String DNI);

	Optional<Ciudadano> findByDNIAndEstado(String DNI, String estado);
	
	Optional<Ciudadano>findByDNIAndEstadoAndIdPlan(String DNI, String estado,Plan plan);

	List<Ciudadano> findAllByDNIAndEstado(String DNI, String estado);

	List<Ciudadano> findAllByDNIAndEstadoAndIdPlan(String DNI, String estado, Plan plan);
	List<Ciudadano> findAllByDNIAndEstadoAndIdPlanAndDeleted(String DNI, String estado, Plan plan,boolean deleted);
	List<Ciudadano> findAllByIdPlan( Plan plan);

	boolean existsByDNI(String DNI);

	Page<Ciudadano> findAll(Specification<Ciudadano> spec, Pageable page);

	Page<Ciudadano> findAllByDeleted(boolean deleted, Specification<Ciudadano> spec, Pageable page);

	long countByIdPlanAndDeleted(Plan plan, boolean deleted);

	Page<Ciudadano> findAllByIdPlanAndDeleted(Specification<Ciudadano> spec, Pageable page, Plan plan, boolean deleted);

	List<Ciudadano> findByEstadoAndContratoEntidadIdOrganismoAndContratoOcupacionIdOcupacion(String estado,
			long idOrganismo, long idOcupacion);

	List<Ciudadano> findByContratoEntidadIdOrganismoAndContratoOcupacionIdOcupacionAndEstadoIn(Long idOrganismo,
			Long idOcupacion, List<String> estados);

	Ciudadano findByEstadoAndDNI(String estado, String DNI);

	List<Ciudadano> findAllByPeriodosVacacionesIsNotNullAndIdPlanIdPlan(Long idPlan);

	@Query("SELECT new com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO(c.estado as estado ,sum(case when c.sexo = \"HOMBRE\" then 1 else 0 end) as hombres ,sum(case when c.sexo = \"MUJER\" then 1 else 0 end) as mujeres) FROM Ciudadano c WHERE c.idPlan.idPlan = ?1 GROUP BY c.estado")
	List<DashBoardEstadoGeneroDTO> findAllByIdPlanIdPlanGroupByEstado(Long idPlan);

	@Query("SELECT new com.melilla.gestPlanes.DTO.DashBoardEstadoGeneroDTO(c.estado as estado ,sum(case when c.sexo = \"HOMBRE\" then 1 else 0 end) as hombres ,sum(case when c.sexo = \"MUJER\" then 1 else 0 end) as mujeres) FROM Ciudadano c WHERE c.idPlan.idPlan = ?1 and c.deleted = 0 GROUP BY c.estado")
	List<DashBoardEstadoGeneroDTO> findAllByIdPlanAndNotDeletedGroupByEstado(Long idPlan);

	List<Ciudadano> findAllByidPlanIdPlanAndBajaLaboralTrue(Long idPlan);

	List<Ciudadano> findAllByidPlanIdPlanAndBajaMaternalTrue(Long idPlan);

	List<Ciudadano> findAllByDNI(String dni);
}
