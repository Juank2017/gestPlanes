package com.melilla.gestPlanes.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.melilla.gestPlanes.model.Organismo;
import com.melilla.gestPlanes.model.OrganismoOcupacion;
import com.melilla.gestPlanes.model.Plan;

public interface OrganismoOcupacionRepository extends JpaRepository<OrganismoOcupacion, Long> {

	@Query(value ="SELECT o FROM OrganismoOcupacion o where o.organismo in ?1 GROUP BY o.organismo, o.ocupacion")
	List<OrganismoOcupacion>findAllAgrupados(Collection<Organismo> organismosPlan);
	
	
	List<OrganismoOcupacion>findAllByOrganismoIdOrganismo(Long idOrganismo);
	
	List<OrganismoOcupacion> findByOrganismoIdOrganismoAndOcupacionIdOcupacion(Long idOrganismo,Long idOcupacion);
	
	Long countByOrganismoIdOrganismoAndOcupacionIdOcupacion(Long idOrganismo,Long idOcupacion);
}
