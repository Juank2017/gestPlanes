package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Documento;

public interface DocumentoRepository extends JpaRepository<Documento, Long>, RevisionRepository<Documento, Long, Long>,JpaSpecificationExecutor<Documento>{

	List<Documento>findAllByCiudadanoIdCiudadano(Long idCiudadano);
	
	List<Documento>findAllByCiudadanoIdCiudadanoAndDeletedFalse(Long idCiudadano);
}
