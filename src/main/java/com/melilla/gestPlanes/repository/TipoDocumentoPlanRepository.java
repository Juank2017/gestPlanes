package com.melilla.gestPlanes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.melilla.gestPlanes.model.TipoDocumentoPlan;

public interface TipoDocumentoPlanRepository extends JpaRepository<TipoDocumentoPlan, Long>{
	Optional<TipoDocumentoPlan> findByTipo(String tipoDocumento);
}
