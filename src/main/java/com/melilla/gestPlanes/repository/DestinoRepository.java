package com.melilla.gestPlanes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.melilla.gestPlanes.model.Destino;

public interface DestinoRepository extends JpaRepository<Destino, Long> {
	
	List<Destino>findAllByIdOrganismoIdOrganismoOrderByDestinoAsc(Long IdOrganismo);
	
	Optional<Destino> findByDestinoAndIdOrganismoIdOrganismo(String destino,Long idOrganismo);

}
