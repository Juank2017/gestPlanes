package com.melilla.gestPlanes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Ciudadano;




public interface CiudadanoRepository extends JpaRepository<Ciudadano, Long> ,RevisionRepository<Ciudadano, Long, Long> {
	
	Optional<Ciudadano> findByDNI(String DNI);
	boolean existsByDNI(String DNI);
	 Page<Ciudadano> findAllByIdPlanIdPlan(Long idPlan,Pageable page);
	

}

