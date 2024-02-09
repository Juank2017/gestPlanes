package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.melilla.gestPlanes.model.OrganismoOcupacion;

public interface OrganismoOcupacionRepository extends JpaRepository<OrganismoOcupacion, Long> {

	@Query("SELECT o FROM OrganismoOcupacion o GROUP BY o.organismo, o.ocupacion")
	List<OrganismoOcupacion>findAllAgrupados();
	
	
	List<OrganismoOcupacion>findAllByOrganismoIdOrganismo(Long idOrganismo);
	
	List<OrganismoOcupacion> findByOrganismoIdOrganismoAndOcupacionIdOcupacion(Long idOrganismo,Long idOcupacion);
	
	Long countByOrganismoIdOrganismoAndOcupacionIdOcupacion(Long idOrganismo,Long idOcupacion);
}
