package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.OrganismoOcupacion;

public interface OrganismoOcupacionRepository extends JpaRepository<OrganismoOcupacion, Long> {

	List<OrganismoOcupacion>findAllByOrganismoIdOrganismo(Long idOrganismo);
}
