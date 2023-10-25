package com.melilla.gestPlanes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import com.melilla.gestPlanes.model.Expediente;
import java.util.List;
import com.melilla.gestPlanes.model.Ciudadano;


public interface ExpedienteRepository
		extends JpaRepository<Expediente, Long>, RevisionRepository<Expediente, Long, Long> {
	
		Expediente findByInteresadosDNI(String DNI);
		
		List<Expediente> findAllByInteresadosContratoOcupacion(String ocupacion);

}
