package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.melilla.gestPlanes.model.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
	
	List<Contrato>findAllByCiudadanoEstadoIn(List<String>estados);

}
