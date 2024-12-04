package com.melilla.gestPlanes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;


import com.melilla.gestPlanes.model.SalarioDetalle;



public interface SalarioDetalleRepository extends JpaRepository<SalarioDetalle, Long>, RevisionRepository<SalarioDetalle, Long, Long>{

	List<SalarioDetalle> findByIdSalarioDetalle(long idSalario);
	List<SalarioDetalle> findAllBySalarioIdSalario(long idSalario);
	
	
}
