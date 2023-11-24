package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.Destino;

public interface DestinoRepository extends JpaRepository<Destino, Long> {
	
	List<Destino>findAllByIdOrganismoIdOrganismo(Long IdOrganismo);

}
